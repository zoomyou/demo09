package com.demo09.dao;

import com.demo09.entity.Job;
import org.apache.ibatis.annotations.Mapper;
import java.util.List;

@Mapper
public interface JobDao {

    /**
     * 发起任务
     */
    void addJob(Job job);

    /**
     * 分配任务
     */
    void schedule(Job job);

    /**
     * 接受任务
     */
    void receiveJob(Job job);

    /**
     * 完成任务
     */
    void finishJob(Job job);

    /**
     * 查找待完成任务
     */
    Job unFinishedJob(String receiver_id);

    /**
     * 查找已被完成任务
     */
    List<Job> finishedJobs(String requester_id);

    /**
     * 根据任务id进行任务查询
     * @return
     */
    Job findById(String job_id);

    /**
     *根据接收者id进行查询
     * @return
     */
    List<Job> receiveJobs(String receiver_id);

    List<Job> find();

}
