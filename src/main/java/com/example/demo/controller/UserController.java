package com.example.demo.controller;

import com.example.demo.entity.*;
import com.example.demo.service.InformationService;
import com.example.demo.service.LoginRecordService;
import com.example.demo.service.ScoreService;
import com.example.demo.service.UserService;
import com.example.demo.util.ImageValidate;
import com.example.demo.util.MD5Utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import javax.imageio.ImageIO;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.awt.image.BufferedImage;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller//@RestController是@Controller @ResponseBody的结合
@RequestMapping("/myUser")
public class UserController {

    @Autowired
    private UserService userService;
    @Autowired
    private LoginRecordService loginRecordService;
    @Autowired
    private InformationService informationService;
    @Autowired
    private ScoreService scoreService;

    /**
    * @Description: 跳转到注册页面
    * @Param: []
    * @return: java.lang.String
    * @Author: Lili Chen
    * @Date: 2018/12/13
    */
    @RequestMapping("getRegister")
    public String  pre_register()throws Exception {
        return "register";
    }

    /**
    * @Description: 注册
    * @Param: [user, model]
    * @return: java.lang.String
    * @Author: Lili Chen
    * @Date: 2018/12/13
    */
    @RequestMapping("register")
    public @ResponseBody String  register(User user,Model model) throws Exception {
        System.out.println(user.getPhone()+"  :"+user.getPassword());
        if(user!=null){
            if(user.getPhone()!=null&&user.getPhone()!=""){
                User user1=userService.queryUserByPhone(user.getPhone());
                if(user1==null){
                    if(user.getPassword()!=null&&user.getPassword()!=""){
                        boolean b= userService.register(user);
                        if(b){
                            User user2=userService.queryUserByPhone(user.getPhone());
                            Score score=new Score();
                            score.setU_id(user2.getId());
                            score.setSc(500);
                            boolean a=scoreService.addScore(score);
                            if(a){
                                return "success";
                            }
                        }else{
                            return "fail";
                        }
                    }
                }
            }
        }
        return "fail";
    }


    /**
    * @Description: 跳转到登录页面
    * @Param: []
    * @return: java.lang.String
    * @Author: Lili Chen
    * @Date: 2018/12/13
    */
    @RequestMapping(value = "/getLogin",method = RequestMethod.GET)
    private String getLogin() throws Exception {
        return "login";
    }


    /**
    * @Description: 登录
    * @Param: [user, model]
    * @return: java.lang.String
    * @Author: Lili Chen
    * @Date: 2018/12/13
    */
    @RequestMapping("login")
    public String  login(User user, Model model,HttpServletResponse response,HttpServletRequest request) throws Exception {
        Date date=new Date();
        if(user!=null){
            LoginRecord record=loginRecordService.queryRecordByPhone(user.getPhone());
            if(record!=null){
                if(record.getFail_num()==3){//第四次进行输入
                    Date date2=record.getLogin_date();
                    int c=(int)((date.getTime()-date2.getTime())/1000);
                    if(c<120){//等待时间未结束
                        model.addAttribute("message","连续三次输入错误,需等待2分钟后再登录");
                        return "login";
                    }else{
                        loginRecordService.deleteRecord(record.getLogin_record_id());//到达等待条件
                    }
                }
            }
            if(user.getPhone()!=null&&user.getPhone()!=""){
                User user1=userService.queryUserByPhone(user.getPhone());
                if(user1!=null){
                    System.out.println("正在登陆："+user.getPassword());
                    boolean b=userService.login(user,date);
                    if(b){
                        Cookie[] cookies= request.getCookies();
                        if(cookies!=null){
                            for(Cookie cookie1:cookies){
                                if(cookie1.getName().equals("userPhone")){
                                    Cookie cookie = new Cookie("userPhone", null);
                                    cookie.setMaxAge(0);
                                }
                            }
                        }

                        Cookie cookie=new Cookie("userPhone",user.getPhone());
                        cookie.setMaxAge(-1);//设置时限
                        cookie.setPath("/"); // 设置路径（默认）
                        response.addCookie(cookie);

                     /*   List<Information> inforList=informationService.queryInformationByUid(user1.getId());*/
                        List<Information> inforList=informationService.findInformationListIndex(0,4,user1.getId());
                        Score score=scoreService.queryScore(user1.getId());
                        model.addAttribute("phone",user.getPhone());
                        model.addAttribute("message"," ");
                        model.addAttribute("inforList",inforList);
                        model.addAttribute("u_id",user1.getId());
                        model.addAttribute("score",score.getSc());
                        model.addAttribute("page",1);
                        return "userMain";
                    }else{
                        //输入错误一次后必定有记录
                        LoginRecord record1=loginRecordService.queryRecordByPhone(user.getPhone());
                        if(record1.getFail_num()==3){//第三次输入错误
                            model.addAttribute("message","连续三次输入错误,需等待2分钟后再登录");
                        }else{
                            model.addAttribute("message","密码错误");
                        }
                        return "login";
                    }
                }
            }
        }
        return "login";
    }


   /* @RequestMapping(value = "/listUser",method = RequestMethod.GET)
    private @ResponseBody Map<String,Object> listUser() throws Exception {
        Map<String,Object> modelMap=new HashMap<String,Object>();
        List<User> userList=userService.queryAllUser();
        modelMap.put("userList",userList);
        return modelMap;
    }*/

   /**
   * @Description: 错误页面
   * @Param: []
   * @return: java.lang.String
   * @Author: Lili Chen
   * @Date: 2018/12/13
   */
    @RequestMapping(value = "/getError")
    private String error() throws Exception {
        return "error";
    }

    /**
    * @Description: 验证手机号码是否存在
    * @Param: [phone]
    * @return: java.lang.String
    * @Author: Lili Chen
    * @Date: 2018/12/13
    */
    @RequestMapping(value = "/validatePhone")
    private @ResponseBody String  validate_phone(String phone) throws Exception {
        User user=userService.queryUserByPhone(phone);
        if(user!=null){
            return "exit";
        }
        return "not_exit";
    }

    /**
    * @Description: 手机短信验证
    * @Param: [phone, request]
    * @return: java.lang.String
    * @Author: Lili Chen
    * @Date: 2018/12/13
    */
    @RequestMapping("sendValidateNumber")
    public @ResponseBody String  send_validate_number(String phone, HttpServletRequest request)throws Exception {
        String code = userService.getCode();//强制类型转换为整数，因为random函数返回double;
        System.out.println("验证码是:"+code);
        HttpSession session=request.getSession();
        session.setMaxInactiveInterval(60);
        session.setAttribute("code",code);
        return code;
    }

    /**
    * @Description: 验证码对比
    * @Param: [massage_number, request]
    * @return: java.lang.String
    * @Author: Lili Chen
    * @Date: 2018/12/13
    */
    @RequestMapping("validateNumber")
    public @ResponseBody String  validate_number(String massage_number,HttpServletRequest request)throws Exception {
        HttpSession session=request.getSession();
        String code= (String) session.getAttribute("code");
        System.out.println("session获取code:"+massage_number);
        System.out.println("session获取code:"+code);
        if(code!=null){
            if(code.equals(massage_number)){
                return "success";
            }
        }
        System.out.println("验证码错误");
        return "fail";
    }
    /**
    * @Description: 生成图片验证码
    * @Param: [massage_number, request, response]
    * @return: java.lang.String
    * @Author: Lili Chen
    * @Date: 2018/12/13
    */
    @RequestMapping("getCaptchaCode")
    public @ResponseBody String  getCaptchaCode(String massage_number, HttpServletRequest request, HttpServletResponse response)throws Exception {
        HttpSession session=request.getSession();
        //设置不缓存图片
        response.setHeader("Pragma", "No-cache");
        response.setHeader("Cache-Control", "No-cache");
        response.setDateHeader("Expires", 0);
        //指定生成的响应图片,一定不能缺少这句话,否则错误.
        response.setContentType("buffImg/jpeg");

        ImageValidate imageValidate=new ImageValidate(80,30,4,2);
        String image_code=imageValidate.getCode();
        BufferedImage buffImg =imageValidate.getBuffImg();
        session.setAttribute("image_code",image_code);
        ImageIO.write(buffImg,"JPEG",response.getOutputStream());
        session.removeAttribute("code");
        return "success";
    }

    /**
    * @Description: 图片验证码验证
    * @Param: [code_number, request]
    * @return: java.lang.String
    * @Author: Lili Chen
    * @Date: 2018/12/15
    */
    @RequestMapping("validateCode")
    public @ResponseBody String  validate_code(String code_number,HttpServletRequest request)throws Exception {
        HttpSession session=request.getSession();
        String code_number2= (String) session.getAttribute("image_code");
        if(code_number.equals(code_number2)){
            return "success";
        }
        return "fail";
    }
    /**
    * @Description: 修改密码
    * @Param: [u_id, password, re_password]
    * @return: java.lang.String
    * @Author: Lili Chen
    * @Date: 2018/12/13
    */
    @RequestMapping("updatePassword")
    public @ResponseBody String update_Password(Integer u_id,String password,String re_password)throws Exception{
        System.out.println(password);
        User user2=userService.queryUserById(u_id);
        if(user2!=null){
            UserVo userVo=new UserVo();
            User user=new User();
            user.setPhone(user2.getPhone());
            user.setPassword(password);
            userVo.setRe_password(re_password);
            userVo.setUser(user);
            boolean b=userService.updatePassword(userVo);
            if(b){
                return "success";
            }
        }

        return "fail";
    }


    @RequestMapping("findUpdatePassword")
    public @ResponseBody String find_update_Password(String phone,String password,String re_password)throws Exception{
        User user2=userService.queryUserByPhone(phone);
        if(user2!=null){
            String password2= MD5Utils.md5(password);
            String re_password2=MD5Utils.md5(re_password);
            UserVo userVo=new UserVo();
            User user=new User();
            user.setPhone(user2.getPhone());
            user.setPassword(password2);
            userVo.setRe_password(re_password2);
            userVo.setUser(user);
            boolean b=userService.updatePassword(userVo);
            if(b){
                return "success";
            }
        }

        return "fail";
    }

}
