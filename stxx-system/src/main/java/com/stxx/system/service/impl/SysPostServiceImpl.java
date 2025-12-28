package com.stxx.system.service.impl;

import java.util.List;
import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.stxx.common.constant.UserConstants;
import com.stxx.common.exception.ServiceException;
import com.stxx.common.utils.StringUtils;
import com.stxx.system.domain.SysPost;
import com.stxx.system.mapper.SysPostMapper;
import com.stxx.system.mapper.SysUserPostMapper;
import com.stxx.system.service.ISysPostService;

/**
 * 岗位信息 服务层处理
 *
 * @author ruoyi
 */
@Service
public class SysPostServiceImpl extends ServiceImpl<SysPostMapper, SysPost> implements ISysPostService {

    private final SysUserPostMapper userPostMapper;

    public SysPostServiceImpl(SysUserPostMapper userPostMapper) {
        this.userPostMapper = userPostMapper;
    }

    /**
     * 查询岗位信息集合
     *
     * @param post 岗位信息
     * @return 岗位信息集合
     */
    @Override
    public List<SysPost> selectPostList(SysPost post) {
        QueryWrapper<SysPost> queryWrapper = new QueryWrapper<>();
        queryWrapper.like(StringUtils.isNotEmpty(post.getPostCode()), "post_code", post.getPostCode())
                    .eq(StringUtils.isNotEmpty(post.getStatus()), "status", post.getStatus())
                    .like(StringUtils.isNotEmpty(post.getPostName()), "post_name", post.getPostName());
        return this.list(queryWrapper);
    }

    /**
     * 查询所有岗位
     *
     * @return 岗位列表
     */
    @Override
    public List<SysPost> selectPostAll() {
        return this.list();
    }

    /**
     * 通过岗位ID查询岗位信息
     *
     * @param postId 岗位ID
     * @return 角色对象信息
     */
    @Override
    public SysPost selectPostById(Long postId) {
        return this.getById(postId);
    }

    /**
     * 根据用户ID获取岗位选择框列表
     *
     * @param userId 用户ID
     * @return 选中岗位ID列表
     */
    @Override
    public List<Long> selectPostListByUserId(Long userId) {
        return baseMapper.selectPostListByUserId(userId);
    }

    /**
     * 校验岗位名称是否唯一
     *
     * @param post 岗位信息
     * @return 结果
     */
    @Override
    public boolean checkPostNameUnique(SysPost post) {
        Long postId = post.getPostId() != null ? post.getPostId() : -1L;
        QueryWrapper<SysPost> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("post_name", post.getPostName()).last("limit 1");
        SysPost info = this.getOne(queryWrapper);
        if (info != null && !info.getPostId().equals(postId)) {
            return UserConstants.NOT_UNIQUE;
        }
        return UserConstants.UNIQUE;
    }

    /**
     * 校验岗位编码是否唯一
     *
     * @param post 岗位信息
     * @return 结果
     */
    @Override
    public boolean checkPostCodeUnique(SysPost post) {
        Long postId = post.getPostId() != null ? post.getPostId() : -1L;
        QueryWrapper<SysPost> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("post_code", post.getPostCode()).last("limit 1");
        SysPost info = this.getOne(queryWrapper);
        if (info != null && !info.getPostId().equals(postId)) {
            return UserConstants.NOT_UNIQUE;
        }
        return UserConstants.UNIQUE;
    }

    /**
     * 通过岗位ID查询岗位使用数量
     * 
     * @param postId 岗位ID
     * @return 结果
     */
    @Override
    public int countUserPostById(Long postId)
    {
        return userPostMapper.countUserPostById(postId);
    }

    /**
     * 删除岗位信息
     *
     * @param postId 岗位ID
     * @return 结果
     */
    @Override
    public int deletePostById(Long postId) {
        return this.removeById(postId) ? 1 : 0;
    }

    /**
     * 批量删除岗位信息
     *
     * @param postIds 需要删除的岗位ID
     * @return 结果
     */
    @Override
    public int deletePostByIds(Long[] postIds) {
        for (Long postId : postIds) {
            SysPost post = this.getById(postId);
            if (countUserPostById(postId) > 0) {
                throw new ServiceException(String.format("%1$s已分配,不能删除", post.getPostName()));
            }
        }
        return this.removeByIds(java.util.Arrays.asList(postIds)) ? 1 : 0;
    }

    /**
     * 新增保存岗位信息
     *
     * @param post 岗位信息
     * @return 结果
     */
    @Override
    public int insertPost(SysPost post) {
        return this.save(post) ? 1 : 0;
    }

    /**
     * 修改保存岗位信息
     *
     * @param post 岗位信息
     * @return 结果
     */
    @Override
    public int updatePost(SysPost post) {
        return this.updateById(post) ? 1 : 0;
    }
}
