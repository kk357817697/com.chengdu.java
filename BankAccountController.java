package com.sundsoft.housefund.admin.bankaccount.controller;

import java.io.IOException;
import java.io.PrintWriter;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import com.sundsoft.housefund.admin.bankaccount.service.IBankAccountService;
import com.sundsoft.sdpubliclibary.util.CommonMethods;
import com.sundsoft.sdpubliclibary.util.Constant;
import net.sf.json.JSONObject;

/**
 * 
 * 项目名称：gjjv7
 * 包名：com.sundsoft.housefund.cashier.account.controller
 * 类名：AccountController
 * @author hefei
 * 创建时间：2016年6月16日上午10:19:47
 * 类的说明：银行账户管理Controller
 *
 */
@Controller
@RequestMapping("/bankAccountController")
public class BankAccountController {
	
	@Autowired
	private IBankAccountService bankAccountService;
	
	/**
	 * 银行账号跳转页面
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/index.do")	
	public String bankAccount(HttpServletRequest request,HttpServletResponse response,ModelMap model){
		model.addAttribute("controller", "bankAccountController");
		return "admin/bank_account/index";
	}

	/**
	 * 列表数据
	 * @param request
	 * @param response
	 * @param model
	 * @throws IOException
	 */
	@RequestMapping(value = "/getList.do",method = RequestMethod.POST)
	@ResponseBody
	public void getBankAccountList(HttpServletRequest request,HttpServletResponse response,	
			 ModelMap model) throws IOException{
		Map<String, Object> params = new HashMap<String, Object>();
		String _search=request.getParameter("_search");
		String page=request.getParameter("page");
		String rows=request.getParameter("rows");
		//排序字段取消
		/*String sidx=request.getParameter("sidx");
		String sord=request.getParameter("sord");*/
		String YHZHMC="";
		String bankRegId="";
//		if(Constant.TRUE.equals(_search.toUpperCase())){
//			String filters_ext=request.getParameter("filters_ext");
//			List<Map<String, String>> list=CommonMethods.jqGrid(_search,filters_ext);
//			if(null != list && !list.isEmpty()){
//				if(list.size()>1)
//				{
//					bankRegId=list.get(1).get("data");
//					yhzhmc=list.get(2).get("data");
//				}
//			}
//		}
		if(Constant.TRUE.equals(_search.toUpperCase())){
			String filters=request.getParameter("filters_ext");
			if (!CommonMethods.isEmptyString(filters)){
				List<Map<String, String>> list=CommonMethods.jqGrid(_search,filters);
				if(null != list && !list.isEmpty()){
					for (int i = 1; i < list.size(); i++) {
						if("bankRegId".equals(list.get(i).get("field"))){
							bankRegId=list.get(i).get("data");
						}
						if("YHZHMC".equals(list.get(i).get("field"))){
							YHZHMC=list.get(i).get("data");
						}						
					}
				}
			}
		}
		if(bankRegId.equals("")){
			bankRegId="0";
		}
		params.put("YHZHMC", YHZHMC);
		params.put("bankRegId", bankRegId);
		//排序字段取消
		/*params.put("orderByName", sidx);
		params.put("isASC", CommonMethods.sord(sord));*/
		params.put("pageIndex", page);
		params.put("pageSize", rows);
		params.put("rowID", "");
		//获取列表数据
		params=bankAccountService.getList(params);
		PrintWriter out = response.getWriter(); 
		JSONObject jsonArray=JSONObject.fromObject(params);
		response.setContentType("application/json;charset=UTF-8");
		out.write(jsonArray.toString());
		out.close();
	}
	
	/**
	 * 保存更改数据方法
	 * @param request
	 * @param response
	 * @param model
	 * @throws IOException
	 * @throws ParseException 
	 */
	@RequestMapping(value = "/save.do",method = RequestMethod.POST)
	@ResponseBody
	public void saveBankAccount(HttpServletRequest request,HttpServletResponse response,
			@RequestParam("BANKACCID")String bankAccId,
        	@RequestParam("BANKNODEID") String bankNodeId,
        	@RequestParam("YHZHHM")String yhzhhm,
        	@RequestParam("YHZHMC")String yhzhmc,
        	@RequestParam("YHDM")String yhdm,
        	@RequestParam("KHRQ")String khrq,
        	@RequestParam("ZHXZ")String zhxz,
        	@RequestParam("TXCHANNEL")String txChannel,
        	@RequestParam("TXORGNO")String txOrgNo,
        	@RequestParam("KMBH")String kmbh,
        	@RequestParam("YHMC")String yhmc,
			 ModelMap model) throws IOException, ParseException{
		Map<String, Object> params = new HashMap<String, Object>();
		//日期格式转换
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		Date opendate  = sdf.parse(khrq); 
		//绑定参数
		params.put("bankAccId", bankAccId);
		params.put("bankNodeId", bankNodeId);
		params.put("yhzhhm", yhzhhm);
		params.put("yhzhmc", yhzhmc);
		params.put("yhdm", yhdm);
		params.put("khrq", opendate); 
		params.put("zhxz", StringUtils.isBlank(zhxz) ? "01" : zhxz);
		params.put("txChannel", txChannel);
		params.put("txOrgNo", txOrgNo);
		params.put("kmbh", kmbh);
		params.put("yhmc", yhmc);
		//获取菜单数据
		params=bankAccountService.save(params);
		PrintWriter out = response.getWriter(); 
		JSONObject jsonArray=JSONObject.fromObject(params);
		response.setContentType("application/json;charset=UTF-8");
		out.write(jsonArray.toString());
		out.close();
	}
	
	/**
	 * 查询单个数据
	 * @param request
	 * @param response
	 * @param model
	 * @throws IOException
	 */
	@RequestMapping(value = "/getById.do",method = RequestMethod.POST)
	@ResponseBody
	public void getBankAccount(HttpServletRequest request,HttpServletResponse response,
			@RequestParam("id")String BankAccountId,
			 @RequestParam("name")String BankAccountName,
			 ModelMap model) throws IOException{
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("bankAccId", BankAccountId);
		params.put("BankAccountName", BankAccountName);
		//获取菜单数据
		params=bankAccountService.getById(params);
		PrintWriter out = response.getWriter(); 
		JSONObject jsonArray=JSONObject.fromObject(params);
		response.setContentType("application/json;charset=UTF-8");
		out.write(jsonArray.toString());
		out.close();
	}
	
	/**
	 * 银行账号管理查询单条数据
	 * @param request
	 * @param response
	 * @param model
	 * @throws IOException
	 */
	@RequestMapping(value = "/getByIdBankAccount.do",method = RequestMethod.POST)
	@ResponseBody
	public void getByIdBankAccount(HttpServletRequest request,HttpServletResponse response,
			@RequestParam("id")String BankAccountId,
			 ModelMap model) throws IOException{
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("bankAccId", BankAccountId);
		params.put("BankAccountName", "0"); //单独查询银行账号管理时候默认的类型为0
		//获取菜单数据
		params=bankAccountService.getById(params);
		PrintWriter out = response.getWriter(); 
		JSONObject jsonArray=JSONObject.fromObject(params);
		response.setContentType("application/json;charset=UTF-8");
		out.write(jsonArray.toString());
		out.close();
	}
	/**
	 * 删除辖区表信息
	 * @param request
	 * @param response
	 * @param model
	 * @throws IOException
	 */
	@RequestMapping(value = "/delById.do",method = RequestMethod.POST)
	@ResponseBody
	public void delBankNode(HttpServletRequest request,HttpServletResponse response,
			 @RequestParam("id")String bankAccId,
			 ModelMap model) throws IOException{
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("bankAccId", bankAccId);
		params=bankAccountService.delById(params);
		PrintWriter out = response.getWriter(); 
		JSONObject jsonArray=JSONObject.fromObject(params);
		response.setContentType("application/json;charset=UTF-8");
		out.write(jsonArray.toString());
		out.close();
	}
	
	/**
	 * 注销---撤销注销银行账户
	 * @param request
	 * @param response
	 * @param model
	 * @throws IOException
	 * @throws ParseException 
	 */
	@RequestMapping(value = "/revertCancel.do",method = RequestMethod.POST)
	@ResponseBody
	public void revertCancel(HttpServletRequest request,HttpServletResponse response,
			 @RequestParam("bankAccId")String bankAccId,
			// @RequestParam("closeDate")String closeDate,
			 ModelMap model) throws IOException, ParseException{
		Map<String, Object> params = new HashMap<String, Object>();
		//得到long类型当前时间
		long l = System.currentTimeMillis();
		//new日期对象
		Date sysj = new Date(l);
		//转换提日期输出格式
		SimpleDateFormat dateFormat4 = new SimpleDateFormat("yyyy/MM/dd");
		String formatsdate = dateFormat4.format(sysj); //系统时间
		Date sydate = dateFormat4.parse(formatsdate);
		/*SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");
		Date date  = sdf.parse(closeDate); */
		params.put("bankAccId", bankAccId);
		params.put("closeDate", sydate);
		params=bankAccountService.bankAccuntLog(params);
		PrintWriter out = response.getWriter(); 
		JSONObject jsonArray=JSONObject.fromObject(params);
		response.setContentType("application/json;charset=UTF-8");
		out.write(jsonArray.toString());
		out.close();
	}
	
	/**
	 * 撤销注销银行账户
	 * @param request
	 * @param response
	 * @param model
	 * @throws IOException
	 * @throws ParseException 
	 */
	@RequestMapping(value = "/revertCancels.do",method = RequestMethod.POST)
	@ResponseBody
	public void revertCancels(HttpServletRequest request,HttpServletResponse response,
			 @RequestParam("bankAccId")String bankAccId,
			 @RequestParam("closeDate")String closeDate,
			 ModelMap model) throws IOException, ParseException{
		Map<String, Object> params = new HashMap<String, Object>();
		//得到long类型当前时间
		//long l = System.currentTimeMillis();
		//new日期对象
		//Date sysj = new Date(l);
		//转换提日期输出格式
	//	SimpleDateFormat dateFormat4 = new SimpleDateFormat("yyyy/MM/dd");
		//String formatsdate = dateFormat4.format(sysj); //系统时间
		/*Date sydate = dateFormat4.parse(formatsdate);*/
		/*SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");
		Date date  = sdf.parse(closeDate); */
		params.put("bankAccId", bankAccId);
		params.put("closeDate", "");
		params=bankAccountService.bankAccuntLog(params);
		PrintWriter out = response.getWriter(); 
		JSONObject jsonArray=JSONObject.fromObject(params);
		response.setContentType("application/json;charset=UTF-8");
		out.write(jsonArray.toString());
		out.close();
	}
}
