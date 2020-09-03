package com.demo09.controller;

import com.alibaba.fastjson.JSONObject;
import com.demo09.entity.Job;
import com.demo09.entity.User;
import com.demo09.service.JobService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@CrossOrigin
@Slf4j
public class ClientController {

    @Autowired
    JobService jobService;

    /**
     * 用于返回未完成的任务对象
     * @param user
     * @return
     */
    @PostMapping("/unFinishedJob")
    public Job unFinishedJob(@RequestBody User user){
        return jobService.unFinishedJob(user.getUser_id());
    }

    /**
     * 用于接受任务开始计时
     * @param job
     * @return
     */
    @PostMapping("/receiveJob")
    public JSONObject receive(@RequestBody Job job){

        JSONObject res = new JSONObject();
        String status = "202", message = "已接受任务请勿重复接受！", judge = "";

        //1.尝试接受任务返回一个用于判断是否接受过任务的变量
        try{
           judge = jobService.startJob(job);
        } catch (Exception e){
            e.printStackTrace();
        }

        //2.如果还未接受，则成功接受
        if (judge.equals("1")){
            status = "200";
            message = "已成功接受任务！";
        }

        res.put("status",status);
        res.put("message",message);

        return res;

    }

    /**
     * 用于完成任务，传递识别结果，停止计时
     * @param job
     * @return
     */
    @PostMapping("/finishJob")
    public JSONObject finish(@RequestBody Job job){

        JSONObject res = new JSONObject();
        String status = "202", messgae = "已完成任务，请勿重复完成！", judge = "2";

        //1.先对任务的状态进行判断
        try{
            judge = jobService.finishJob(job);
        } catch (Exception e){
            e.printStackTrace();
        }

        //2.对变量进行判断
        if (judge.equals("1")){
            RequestController.result = job.getCaptcha_result();
            System.out.println("id为"+job.getReceiver_id()+"的用户得分为:");
            System.out.println(jobService.mark(job.getReceiver_id()));
            status = "200";
            messgae = "已成功完成任务";
        }

        //3.对变量进行判断
        if (judge.equals("2")){
            status = "201";
            messgae = "还未接受任务，请先接受任务！";
        }

        res.put("status",status);
        res.put("message",messgae);

        return res;
    }
}
