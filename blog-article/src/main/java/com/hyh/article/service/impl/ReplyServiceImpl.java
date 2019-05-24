package com.hyh.article.service.impl;

import com.hyh.article.mapper.ReplyMapper;
import com.hyh.article.service.ReplyService;
import com.hyh.pojo.Reply;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class ReplyServiceImpl implements ReplyService {

    @Autowired
    ReplyMapper replyMapper;

    @Override
    public int insertReply(Reply reply){
        reply.setCreateTime(new Date());
        return replyMapper.insert(reply);
    }
}
