package com.demo09.service;

import com.demo09.dao.JobDao;
import com.demo09.dao.UserDao;
import com.demo09.entity.Job;
import com.demo09.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.util.List;

@Service
@Transactional
public class JobService {

    @Autowired
    JobDao jobDao;

    @Autowired
    UserDao userDao;

    // 发起任务
    public String addJob(Job job){

        String res = "-1";

        //1.先进行任务的调度看看是否有空闲的打码用户在线
        String judge = schedule(job);

        //2.如果返回值不为-1则在数据库中进行任务的增加并返回接收者的id
        if (!judge.equals("-1")){
            job.setReceiver_id(judge);
            jobDao.addJob(job);
            res = judge;
        }

        return res;

    }

    // 任务调度
    private String schedule(Job job){

        //1.查找空闲用户
        List<User> list = userDao.findAvailable();

        //2.如果没有空闲用户则直接返回-1
        if (list.isEmpty()){
            return "-1";
        }

        //3.如果存在空闲用户则将其取出获取其id值
        User user = list.remove(0);

        //4.修改用户状态
        userDao.UnAvailable(user.getUser_id());

        //5.返回空闲用户的id
        return user.getUser_id();

    }

    // 开始任务
    public String startJob(Job job){

        //执行结果返回，0代表已经接受过该任务
        String res = "0";

        //1.先查询是否已经接受了任务
        Job judge = jobDao.findById(job.getJob_id());

        if (judge.getJob_status().equals("0")){
            //2.如果处于未完成状态，则为其设置接受时间并修改状态
            job.setReceive_time(new Timestamp(System.currentTimeMillis()));
            jobDao.receiveJob(job);
            res = "1";
        }

        return res;
    }

    // 完成任务
    public String finishJob(Job job){

        String res = "2";

        //1.先判断任务是否已经完成避免重复完成
        Job judge = new Job();
        try {
            judge = jobDao.findById(job.getJob_id());
        } catch (Exception e) {
            e.printStackTrace();
        }

        //2.如果未完成，则进行之后的各个步骤
        if (judge.getJob_status().equals("1")){

            //3.为任务设置完成时间和状态
            job.setFinish_time(new Timestamp(System.currentTimeMillis()));
            job.setJob_status("2");
            try {
                jobDao.finishJob(job);
                userDao.Available(job.getReceiver_id());
                res = "1";
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        //4.任务还未接受
        if (judge.getJob_status().equals("0")){
            res = "0";
        }

        return res;
    }

    //已经完成的任务的列表
    public String mark(String user_id){

        String res = "";
        List<Job> list = null;

        try {
            list = jobDao.receiveJobs(user_id);
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (list.isEmpty()){
            res+="0";
        } else {
            int count = list.size();
            long time = 0;
            for (int i = 0;i<list.size();i++){
                Job job = list.get(i);
                time = time + ( job.getFinish_time().getTime() - job.getReceive_time().getTime() );
            }
            double temp1 = (double)(count*108);
            double temp2 = (double)time/10000.0;
            double mark = 0;
            if ((temp1-temp2)>0){
                mark = Math.log(temp1-temp2);
                mark *= 10;
            }
            res+=mark;
        }

        return res;

    }

    //未完成的任务
    public Job unFinishedJob(String user_id){
        return jobDao.unFinishedJob(user_id);
    }

    //已被完成的任务
    public List<Job> finishedJobs(String requester_id){
        List<Job> list = jobDao.finishedJobs(requester_id);
        return list;
    }

}
