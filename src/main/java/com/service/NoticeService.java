package com.service;

import com.googlecode.ehcache.annotations.Cacheable;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.*;


/**
 * @author: yangyang 2013年10月21日
 * @since JDK 1.6
 */
@Service("XXXNoticeService")
public class NoticeService {
    private static Log logger = LogFactory.getLog(NoticeService.class);
    @Resource(name = "XXXNoticeDao")
    private NoticeDao dao;

    /**
     * status = 0 指未删除
     */
    @Cacheable(value = "cacheTest", key = "'noticelist'")
    public List<Notice> topN(int begin, int end) {
        LinkedHashMap<String, String> orderby = new LinkedHashMap<String, String>();
        orderby.put("publish_time", "desc");
        Map<String, String> where = new HashMap<String, String>();
        where.put(dao.STATUS + " = ? ", dao.NORMAL_CODE.toString());
        //TODO:delete
        System.out.println("list:");
        logger.info("[list ]");
        return dao.find(where, orderby, begin, end);
    }

    @CacheEvict(value = "cacheTest", key = "'noticelist'")
    public void delete(String id) {
        //TODO:delete
        System.out.println("delete:");
        logger.info("delete ");
        dao.delete(id, false);
    }

    @CacheEvict(value = "cacheTest", allEntries = true)
    public void save(Notice notice) {
        notice.setRowid(ToolUtil.getUUID());
        notice.setStatus(dao.NORMAL_CODE);
        notice.setPublish_time(new Date());
        // TODO:yeyid的获得方式
        notice.setYey_id("123");
        dao.insert(notice);
        //TODO:delete
        System.out.println("save:");
        logger.info("save ");
    }

    public Notice get(String id) {
        return dao.findById(id);
    }

    //@CachePut(value = "cacheTest",key="#notice.getRowid()")
    public void update(Notice notice) {
        Map<String, String> set = new HashMap<String, String>();
        LinkedHashMap<String, String> where = new LinkedHashMap<String, String>();
        set.put("title", notice.getTitle());
        set.put("author", notice.getAuthor());
        set.put("content", notice.getContent());
        where.put(dao.getIdColumnName() + "=?", notice.getRowid());
        dao.update(set, where);
        System.out.println("update:");
        logger.info("update ");
    }

}