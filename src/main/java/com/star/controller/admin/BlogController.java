package com.star.controller.admin;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.star.entity.Blog;
import com.star.entity.Type;
import com.star.entity.User;
import com.star.queryvo.BlogQuery;
import com.star.queryvo.SearchBlog;
import com.star.queryvo.ShowBlog;
import com.star.service.BlogService;
import com.star.service.TypeService;
import org.junit.Test;
import org.omg.PortableInterceptor.INACTIVE;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.util.*;

/**
 * @Description: 博客管理控制器
 * @Author: ONESTAR
 * @Date: Created in 12:08 2020/3/27
 * @QQ群: 530311074
 * @URL: https://onestar.newstar.net.cn/
 */
@Controller
@RequestMapping("/admin")
public class BlogController {

    @Autowired
    private BlogService blogService;

    @Autowired
    private TypeService typeService;

    //分页查询博客列表
    @RequestMapping("/blogs")
    public String blogs(Model model, @RequestParam(defaultValue = "1",value = "pageNum") Integer pageNum){
        //按照排序字段 升序
        String orderBy = "update_time asc";
        PageHelper.startPage(pageNum,10,orderBy);
        List<BlogQuery> list = blogService.getAllBlog();
        PageInfo<BlogQuery> pageInfo = new PageInfo<BlogQuery>(list);
        model.addAttribute("types",typeService.getAllType());
        model.addAttribute("pageInfo",pageInfo);
        return "admin/blogs";
    }

    //跳转博客新增页面
    @GetMapping("/blogs/input")
    public String input(Model model) {
        model.addAttribute("types",typeService.getAllType());
        model.addAttribute("blog", new Blog());
        return "admin/blogs-input";
    }

//    博客新增
    @PostMapping("/blogs")
    public String post(Blog blog, RedirectAttributes attributes, HttpSession session){
        //新增页面中传输过来的blog对象还缺少一系列属性，比如user对象
        blog.setUser((User) session.getAttribute("user"));
        //设置blog的type
        blog.setType(typeService.getType(blog.getType().getId()));
        //设置blog中typeId属性
        blog.setTypeId(blog.getType().getId());
        //设置用户id
        blog.setUserId(blog.getUser().getId());
        int b = blogService.saveBlog(blog);

        if(b == 0){
            attributes.addFlashAttribute("message", "新增失败");
        }else {
            attributes.addFlashAttribute("message", "新增成功");
        }
        return "redirect:/admin/blogs";
    }

//    删除文章
    @GetMapping("/blogs/{id}/delete")
    public String delete(@PathVariable Long id, RedirectAttributes attributes) {
        blogService.deleteBlog(id);
        attributes.addFlashAttribute("message", "删除成功");
        return "redirect:/admin/blogs";
    }

//    跳转编辑修改文章
    @GetMapping("/blogs/{id}/input")
    public String editInput(@PathVariable Long id, Model model) {
        ShowBlog blogById = blogService.getBlogById(id);
        List<Type> allType = typeService.getAllType();
        model.addAttribute("blog", blogById);
        model.addAttribute("types", allType);
        return "admin/blogs-input";
    }

//    编辑修改文章
    @PostMapping("/blogs/{id}")
    public String editPost(@Valid ShowBlog showBlog, RedirectAttributes attributes) {
        int b = blogService.updateBlog(showBlog);
        if(b ==0){
            attributes.addFlashAttribute("message", "修改失败");
        }else {
            attributes.addFlashAttribute("message", "修改成功");
        }
        return "redirect:/admin/blogs";
    }

//    搜索博客 将搜索栏中的属性封装成一个新的VO SearchBlog
    @PostMapping("/blogs/search")
    public String search(SearchBlog searchBlog, Model model,
                         @RequestParam(defaultValue = "1",value = "pageNum") Integer pageNum) {
        List<BlogQuery> blogBySearch = blogService.getBlogBySearch(searchBlog);
        PageHelper.startPage(pageNum, 10);
        PageInfo<BlogQuery> pageInfo = new PageInfo<>(blogBySearch);
        model.addAttribute("pageInfo", pageInfo);
        return "admin/blogs :: blogList";
    }


    @Test
    public void test () {
        List<Integer> list = new ArrayList();
        list.add(1);
        list.add(2);
        list.add(3);
        list.add(6);

        List<Integer> listclone = new ArrayList();

        listclone.add(4);
        listclone.add(5);
        listclone.add(7);

        List<Integer> newlist = new ArrayList<>();
        for (int i = 0; i < list.size(); i++) {
            boolean flag = false;
            for (int j = 0; j < listclone.size(); j++) {
                if (list.get(i) == listclone.get(j)) {
                    newlist.add(listclone.get(j));
                    flag = true;
                    listclone.remove(listclone.get(j));
                    j--;
                    break;
                }
            }
            if (!flag) {
                newlist.add(list.get(i));
            }
        }
        System.out.println(list);
        if (listclone.size() > 0) newlist.addAll(listclone);
        System.out.println(listclone);
        System.out.println(newlist);
    }


    @Test
    public void test2 () {
        Set<String> mergeSet = new HashSet<>();
        List<String> source = new ArrayList<>();
        List<String> target = new ArrayList<>();
        source.add("a");
        source.add("b");
        source.add("c");

        target.add("a");
        target.add("b");
        target.add("c");
        if (source.size() > 0) mergeSet.addAll(source);
        if (target.size() > 0) mergeSet.addAll(target);
        System.out.println(mergeSet);
        System.out.println(source.toString().equals(target.toString()));



    }
}


