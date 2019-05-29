package com.hyh.article.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.hyh.article.client.UserClient;
import com.hyh.article.mapper.ArticleMapper;
import com.hyh.article.mapper.NoticeMapper;
import com.hyh.article.service.NoticeService;
import com.hyh.pojo.Notice;
import com.hyh.pojo.User;
import com.hyh.pojo.Vo.NoticeVo;
import com.hyh.pojo.Vo.PageResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class NoticeServiceImpl implements NoticeService {

    @Autowired
    private NoticeMapper noticeMapper;
    @Autowired
    private ArticleMapper articleMapper;
    @Autowired
    private UserClient userClient;

    @Override
    public PageResult<NoticeVo> listMyNotices(Integer pageCur, Integer pageSize, Integer status, User user) {
        // 开始分页
        PageHelper.startPage(pageCur, pageSize);
        // 查询我浏览的记录 并且状态为1的
        Example example = new Example(Notice.class);
        if (status==2){
            example.createCriteria().andEqualTo("receiverId",user.getId());
        } else {
            example.createCriteria().andEqualTo("receiverId",user.getId()).andEqualTo("status",status);
        }
        example.setOrderByClause("create_time DESC");
        // 查询
        Page<Notice> noticeList = (Page<Notice>) noticeMapper.selectByExample(example);
        // 根据查询结果 封装文章Vo
        List<NoticeVo> result = noticeList.stream().map(notice -> {
            NoticeVo noticeVo = new NoticeVo();
            // 设置文章标题  发消息人名字
            noticeVo.setId(notice.getId());
            noticeVo.setArticleId(notice.getArticleId());
            noticeVo.setType(notice.getType());
            noticeVo.setArticleName(articleMapper.getArticleTitleById(notice.getArticleId()));
            ResponseEntity<String> senderName = userClient.getUserNameById(notice.getSenderId());
            noticeVo.setSenderName(senderName.getBody());
            noticeVo.setReceiverName(user.getName());
            return noticeVo;
        }).collect(Collectors.toList());
        // 返回结果
        return new PageResult<NoticeVo>(result.size(),result);
    }

    @Override
    public int updateNotices(Long[] ids,Integer status) {
        Notice notice = null;
        int count = 0;
        for (Long id: ids) {
            notice = getNoticeById(id);
            notice.setStatus(status);
            count += noticeMapper.updateByPrimaryKeySelective(notice);
        }
        return count;
    }

    @Override
    public Notice getNoticeById(Long id) {
        return noticeMapper.selectByPrimaryKey(id);
    }

    @Override
    public int deleteNotices(Long[] ids) {
        int count = 0;
        for (Long id: ids) {
            count += noticeMapper.deleteByPrimaryKey(id);
        }
        return count;
    }

    @Override
    public int countNotice(User user) {
        Example example = new Example(Notice.class);
        example.createCriteria().andEqualTo("receiverId",user.getId()).andEqualTo("status",0);
        return noticeMapper.selectCountByExample(example);
    }
}
