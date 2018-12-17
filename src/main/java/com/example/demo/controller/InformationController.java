package com.example.demo.controller;

import com.example.demo.entity.Information;
import com.example.demo.entity.Score;
import com.example.demo.entity.User;
import com.example.demo.service.InformationService;
import com.example.demo.service.ScoreService;
import com.example.demo.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;

@Controller
@RequestMapping("/myInformation")
public class InformationController {
    @Resource
    private InformationService informationService;
    @Resource
    private ScoreService scoreService;
    @Resource
    private UserService userService;


    /**
    * @Description:  用户主界面
    * @Param: [model, score, u_id]
    * @return: java.lang.String
    * @Author: Lili Chen
    * @Date: 2018/12/17
    */
    @RequestMapping("userMain")
    public String user_main(Model model, Integer score, Integer u_id) throws Exception {
        System.out.println("第二"+score);
      /*  List<Information> inforList = informationService.queryInformationByUid(u_id);*/
        List<Information> inforList=informationService.findInformationListIndex(0,4,u_id);
        model.addAttribute("u_id", u_id);
        model.addAttribute("inforList", inforList);
        model.addAttribute("score",score);
        model.addAttribute("page",1);
        return "userMain";
    }


    /**
    * @Description: 跳转到增加消息的页面
    * @Param: [model, u_id]
    * @return: java.lang.String
    * @Author: Lili Chen
    * @Date: 2018/12/17
    */
    @RequestMapping("preAddInformation")
    public String pre_addInformation(Model model, Integer u_id) throws Exception {
        Score score=scoreService.queryScore(u_id);
        model.addAttribute("u_id", u_id);
        model.addAttribute("score",score.getSc());
        return "addInformation";
    }


    /**
    * @Description: 增加消息
    * @Param: [model, information]
    * @return: java.lang.String
    * @Author: Lili Chen
    * @Date: 2018/12/17
    */
    @RequestMapping("addInformation")
    public String addInformation(Model model, Information information) throws Exception {
        Date date = new Date();
        information.setCreatetime(date);
        information.setTitle(information.getTitle());
        information.setContent(information.getContent());
        boolean b = informationService.addInformation(information);
        if (b) {
            Score score=scoreService.queryScore(information.getU_id());
            return "redirect:userMain?score="+score.getSc()+"&u_id="+information.getU_id();
        }
        model.addAttribute("u_id", information.getU_id());
        model.addAttribute("missMessage", "添加失败");
        return "addInformation";

    }


    /**
    * @Description:  删除消息
    * @Param: [model, infor_id]
    * @return: java.lang.String
    * @Author: Lili Chen
    * @Date: 2018/12/17
    */
    @RequestMapping("deleteInformation")
    public @ResponseBody
    String deleteInformation(Model model, Integer infor_id) throws Exception {
        boolean b = informationService.deleteInformation(infor_id);
        if (b) {
            return "success";
        }
        return "fail";
    }

    /**
    * @Description: 跳转到编辑页面
    * @Param: [model, infor_id]
    * @return: java.lang.String
    * @Author: Lili Chen
    * @Date: 2018/12/17
    */
    @RequestMapping("preEditInformation")
    public String pre_editInformation(Model model, Integer infor_id) throws Exception {
        Information information=informationService.queryInformationById(infor_id);
        Score score=scoreService.queryScore(information.getU_id());
        model.addAttribute("information",information);
        model.addAttribute("score",score.getSc());
        return "edit";
    }

    /**
    * @Description:  修改消息
    * @Param: [model, information]
    * @return: java.lang.String
    * @Author: Lili Chen
    * @Date: 2018/12/17
    */
    @RequestMapping("editInformation")
    public @ResponseBody
    String editInformation(Model model, Information information) throws Exception {
        System.out.println("输出时间"+information.getCreatetime());
        boolean b = informationService.updateInformation(information);
        if (b) {
            return "success";
        }
        return "fail";
    }

    @RequestMapping("queryInformationList")
    public String queryInformationList(Model model,Integer page,Integer u_id) throws Exception {
        int beginIndex=0;
        int size=4;
        int count=informationService.getCount(u_id);//总共的记录
        int pageCount=count/4;//总共的页数
        if(count%4!=0){
            pageCount++;
        }
        if(page<1){
            page=1;
        }
        if(page>pageCount){
            page=pageCount;
        }
        beginIndex=(page-1)*size;
        List<Information> inforList = informationService.findInformationListIndex(beginIndex,size,u_id);
        model.addAttribute("inforList", inforList); //当前页的消息列表
        model.addAttribute("page",page);//当前的页数
        model.addAttribute("pageCount", pageCount);//总共的页数

        Score score=scoreService.queryScore(u_id);
        User user=userService.queryUserById(u_id);
        model.addAttribute("phone",user.getPhone());
        model.addAttribute("message"," ");
        model.addAttribute("u_id",user.getId());
        model.addAttribute("score",score.getSc());
        return "userMain";

    }
}
