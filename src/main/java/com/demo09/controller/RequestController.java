package com.demo09.controller;

import com.alibaba.fastjson.JSONObject;
import com.demo09.entity.Job;
import com.demo09.entity.User;
import com.demo09.service.JobService;
import com.demo09.util.CaptchaClassify;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.sql.Timestamp;
import java.util.List;
import java.util.UUID;

@RestController
@CrossOrigin
@Slf4j
public class RequestController {

    private String filePath="C:/Users/10842/Desktop/demo09/src/main/resources/static/img/";

    @Autowired
    JobService jobService;

    /**
     * 查看所有的完成的历史任务
     * @param user
     * @return
     */
    @PostMapping("/finishedJobs")
    public List<Job> finishedJobs(@RequestBody User user){
        System.out.println(user.getUser_id());
        List<Job> list = jobService.finishedJobs(user.getUser_id());
        return list;
    }

    /**
     * 用于添加任务到数据库并将任务下载至本地
     * @param job
     * @param photo
     * @return
     */
    @PostMapping("/addJob")
    public JSONObject addJob(Job job, MultipartFile photo){

        JSONObject res = new JSONObject();
        String status = "202", message = "无在线的打码用户请稍后重试！";

        //1.获取文件的名称信息
        String originalFileName = photo.getOriginalFilename();
        //System.out.println(originalFileName);
        String classInfo = CaptchaClassify.classify(originalFileName);
        String newFileName = UUID.randomUUID()+originalFileName;
        //System.out.println(newFileName);
        String newFilePath = filePath + newFileName;
        //System.out.println(newFilePath);
        String src = newFilePath;

        //2.为任务设置字段数据
        job.setSubtype_id(classInfo);
        job.setCaptcha_src(newFilePath);
        job.setJob_status("0");
        job.setRequest_time(new Timestamp(System.currentTimeMillis()));

        //3.获取服务层的相应信息
        String judge = jobService.addJob(job);

        //4.服务器创建文件夹
        File file = new File(filePath);
        if (!file.exists()){
            file.mkdir();
        }

        //5.对服务层返回信息进行判断
        if (!judge.equals("-1")){

            //6.如果返回可用信息则将图片保存至服务器
            try {
                photo.transferTo(new File(newFilePath));

            } catch (IllegalStateException e){
                e.printStackTrace();
            } catch (IOException e){
                e.printStackTrace();
            }

            status = "200";
            message = "发起任务成功！";

        }

        res.put("status",status);
        res.put("message",message);
        res.put("receiverId",judge);
        res.put("type",classInfo);

        return res;
    }

}
