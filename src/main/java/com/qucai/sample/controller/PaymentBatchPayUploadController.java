package com.qucai.sample.controller;

import com.qucai.sample.entity.FinanceProduct;
import com.qucai.sample.entity.OrganizationInfo;
import com.qucai.sample.entity.PaymentBatchPayUpload;
import com.qucai.sample.exception.ExRetEnum;
import com.qucai.sample.service.*;
import com.qucai.sample.util.IdCardUtil;
import com.qucai.sample.util.JsonBizTool;
import com.qucai.sample.util.ShiroSessionUtil;
import com.qucai.sample.util.Tool;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.interceptor.TransactionAspectSupport;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.net.URLDecoder;
import java.text.SimpleDateFormat;
import java.util.*;


@SuppressWarnings("unused")
@Controller
@RequestMapping(value = "/PaymentBatchPayUploadController")
public class PaymentBatchPayUploadController {


    // 必须把new financeProduct的列进行全面修改, 新建financeProductService

    @Autowired
    private PersonalInfoService personalInfoService; //申明一个对象

    @Autowired
    private OrganizationInfoService organizationInfoService;

    @Autowired
    private FinanceProductService financeProductService; //申明一个对象

    @Autowired
    private StaffPrepayApplicationService staffPrepayApplicationService; // 申明一个对象

    @Autowired
    private PaymentBatchPayUploadService paymentBatchPayUploadService;

    @Autowired
    private PaymentBatchPayUploadStatusService paymentBatchPayUploadStatusService;


    private static boolean judegExcelEdition(String fileName){
        if (fileName.matches("^.+\\.(?i)(xls)$")){
            return false;
        }else {
            return true;
        }
    }
    //-----------------------------------MVC 的控制器----------------------
    //Controller为：

    @ModelAttribute
    public PaymentBatchPayUpload get(@RequestParam(required = false) String t_txn_P_Mobil) {
        PaymentBatchPayUpload entity = null;
        if (entity == null) {
            entity = new PaymentBatchPayUpload();
        }
        return entity;
    }

    @RequestMapping(value = {"paymentBatchPayUploadNew"})
    public String PaymentBatchPayUploadNew(HttpServletRequest request,HttpServletResponse response, Model model,String batch_payment_company){
        Map<String, Object> map = new HashMap<String, Object>();
        Map<String, Object> paramMap = new HashMap<String, Object>();
        Map<String, Object> paramSearchMap = new HashMap<String, Object>();

        batch_payment_company = ShiroSessionUtil.getLoginSession().getCompany_name();
        String t_O_OrgName = batch_payment_company,t_P_Company = batch_payment_company; // pending on agent or not agent check
        if (batch_payment_company.equalsIgnoreCase("all")){
            paramMap.put("t_P_VendorEmployeeName","ALL");
            paramSearchMap.put("batch_payment_company","ALL");
            paramSearchMap.put("batch_payment_vendorcompany","ALL");
            List<OrganizationInfo> OrganizationInfo = organizationInfoService.findAllName(paramMap);
            List<PaymentBatchPayUpload> paymentBatchPayUpload = paymentBatchPayUploadService.SelectCompanyBatch(paramSearchMap);
            if(OrganizationInfo.size() == 0){
                OrganizationInfo organizationInfoEmpty = new OrganizationInfo();
                organizationInfoEmpty.setT_O_OrgName(batch_payment_company);
                OrganizationInfo.add(organizationInfoEmpty);
            }
            if(paymentBatchPayUpload.size() == 0){
                PaymentBatchPayUpload paymentBatchPayUploadEmpty = new PaymentBatchPayUpload();
                paymentBatchPayUploadEmpty.setBatch_payment_batchID("<---- 请先上传人员 ---->");
                paymentBatchPayUpload.add(paymentBatchPayUploadEmpty);
            }
            model.addAttribute("paymentBatchPayUpload", paymentBatchPayUpload);
            model.addAttribute("OrganizationInfo", OrganizationInfo);
        }else{
            OrganizationInfo organizationInfo = organizationInfoService.selectAgencyName(t_O_OrgName);
            if (organizationInfo.getT_O_listOrg().equalsIgnoreCase("on")){
                paramSearchMap.put("t_FProd_OrgInfo", null);
                paramSearchMap.put("t_FProd_VendorOrgName",batch_payment_company);
                paramSearchMap.put("batch_payment_company", null);
                paramSearchMap.put("batch_PB_vendorcompany", organizationInfo.getT_O_OrgPending());
                paramSearchMap.put("t_P_VendorEmployeeName", organizationInfo.getT_O_OrgPending());
                paramMap.put("t_O_VendorOrgName",organizationInfo.getT_O_OrgPending());
                List<OrganizationInfo> OrganizationInfo = organizationInfoService.findOrgName(paramMap);
                List<FinanceProduct> FinanceProduct = financeProductService.findSearchList(paramSearchMap);
                List<PaymentBatchPayUpload> paymentBatchPayUpload = paymentBatchPayUploadService.SelectCompanyBatch(paramSearchMap);
                if(OrganizationInfo.size() == 0){
                    OrganizationInfo organizationInfoEmpty = new OrganizationInfo();
                    organizationInfoEmpty.setT_O_OrgName(batch_payment_company);
                    OrganizationInfo.add(organizationInfoEmpty);
                }
                if(FinanceProduct.size() == 0){
                    FinanceProduct financeProductEmpty = new FinanceProduct();
                    financeProductEmpty.setT_FProd_Name("<---- 请先添加产品 ---->");
                    FinanceProduct.add(financeProductEmpty);
                }
                if(paymentBatchPayUpload.size() == 0){
                    PaymentBatchPayUpload paymentBatchPayUploadEmpty = new PaymentBatchPayUpload();
                    paymentBatchPayUploadEmpty.setBatch_payment_batchID("<---- 请先上传人员 ---->");
                    paymentBatchPayUpload.add(paymentBatchPayUploadEmpty);
                }
                model.addAttribute("paymentBatchPayUpload", paymentBatchPayUpload);
                model.addAttribute("OrganizationInfo", OrganizationInfo);
                model.addAttribute("FinanceProduct", FinanceProduct);
            }else {
                paramSearchMap.put("t_FProd_OrgInfo", batch_payment_company);//get all prod list
                paramSearchMap.put("batch_payment_vendorcompany", null); // put vendororg name null
                paramSearchMap.put("batch_payment_company", batch_payment_company);// put org name real name
                List<FinanceProduct> FinanceProduct = financeProductService.findSearchList(paramSearchMap);
                List<PaymentBatchPayUpload> paymentBatchPayUpload = paymentBatchPayUploadService.SelectCompanyBatch(paramSearchMap);
                if(FinanceProduct.size() == 0){
                    FinanceProduct financeProductEmpty = new FinanceProduct();
                    financeProductEmpty.setT_FProd_Name("<---- 请先添加产品 ---->");
                    FinanceProduct.add(financeProductEmpty);
                }
                if(paymentBatchPayUpload.size() == 0){
                    PaymentBatchPayUpload paymentBatchPayUploadEmpty = new PaymentBatchPayUpload();
                    paymentBatchPayUploadEmpty.setBatch_payment_batchID("<---- 请先上传人员 ---->");
                    paymentBatchPayUpload.add(paymentBatchPayUploadEmpty);
                }
                List<OrganizationInfo> organizationInfoSelf = new ArrayList<OrganizationInfo>();
                OrganizationInfo organizationInfoEmpty = new OrganizationInfo();
                organizationInfoEmpty.setT_O_OrgName(batch_payment_company);
                organizationInfoSelf.add(organizationInfoEmpty);
                model.addAttribute("paymentBatchPayUpload", paymentBatchPayUpload);
                model.addAttribute("OrganizationInfo", organizationInfoSelf);
                model.addAttribute("FinanceProduct", FinanceProduct);
            }
        }
        model.addAttribute("t_FProd", batch_payment_company);
        model.addAttribute("batch_payment_company", batch_payment_company);

        String flag = "failure";
        return "paymentBatchPay/paymentBatchPayUploadNew";
    }


    private void add(OrganizationInfo batch_payment_company) {
        // TODO Auto-generated method stub

    }

    @RequestMapping(value = "paymentBatchPayUploadBatchChange")
    @ResponseBody
    public String paymentBatchPayUploadBatchChange(String t_PIBU_Orgname, HttpServletRequest request,
                                                     HttpServletResponse response, Model model) {
        Map<String, Object> rs = new HashMap<String, Object>();
        Map<String, Object> paramMap = new HashMap<String, Object>();
        Map<String, Object> paramSearchMap = new HashMap<String, Object>();
        model.addAttribute("t_Org_name", t_PIBU_Orgname);
        String t_O_OrgName = ShiroSessionUtil.getLoginSession().getCompany_name();
        String batch_payment_company = null;
        try {
            if(t_PIBU_Orgname == null){
                t_PIBU_Orgname =  ShiroSessionUtil.getLoginSession().getCompany_name();
                batch_payment_company = t_PIBU_Orgname;
            }else {
                batch_payment_company = URLDecoder.decode(t_PIBU_Orgname, "UTF-8");
            }
        } catch (UnsupportedEncodingException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        if (t_O_OrgName.equalsIgnoreCase("all")){
            OrganizationInfo organizationInfo = organizationInfoService.selectAgencyName(batch_payment_company);
            if(organizationInfo.getT_O_listOrg() == "on"){
                paramSearchMap.put("batch_payment_vendorcompany", batch_payment_company);
                paramSearchMap.put("batch_payment_company", null);
            }else{
                paramSearchMap.put("batch_payment_vendorcompany", null);
                paramSearchMap.put("batch_payment_company", batch_payment_company);
            }
            List<PaymentBatchPayUpload> paymentBatchPayUpload = paymentBatchPayUploadService.SelectCompanyBatch(paramSearchMap);
            model.addAttribute("paymentBatchPayUpload", paymentBatchPayUpload);
            rs.put("paymentBatchPayUpload", paymentBatchPayUpload);
        }else{
            OrganizationInfo organizationInfo = organizationInfoService.selectAgencyName(t_O_OrgName);
            if (organizationInfo.getT_O_listOrg().equalsIgnoreCase("on")){
                paramSearchMap.put("batch_payment_company", batch_payment_company);
                paramSearchMap.put("batch_payment_vendorcompany", t_O_OrgName);
                List<PaymentBatchPayUpload> paymentBatchPayUpload = paymentBatchPayUploadService.SelectCompanyBatch(paramSearchMap);
                if(paymentBatchPayUpload.size() == 0){
                    PaymentBatchPayUpload paymentBatchPayUploadEmpty = new PaymentBatchPayUpload();
                    paymentBatchPayUploadEmpty.setBatch_payment_batchID("<---- 请先上传人员 ---->");
                    paymentBatchPayUpload.add(paymentBatchPayUploadEmpty);
                }
                model.addAttribute("paymentBatchPayUpload", paymentBatchPayUpload);
                rs.put("paymentBatchPayUpload", paymentBatchPayUpload);
                //input Personal into Org
            }else{
                paramSearchMap.put("batch_PB_vendorcompany", null);
                paramSearchMap.put("batch_PB_company", t_O_OrgName);
                List<PaymentBatchPayUpload> paymentBatchPayUpload = paymentBatchPayUploadService.SelectCompanyBatch(paramSearchMap);
                if(paymentBatchPayUpload.size() == 0){
                    PaymentBatchPayUpload paymentBatchPayUploadEmpty = new PaymentBatchPayUpload();
                    paymentBatchPayUploadEmpty.setBatch_payment_batchID("<---- 请先上传人员 ---->");
                    paymentBatchPayUpload.add(paymentBatchPayUploadEmpty);
                }
                model.addAttribute("paymentBatchPayUpload", paymentBatchPayUpload);
                rs.put("paymentBatchPayUpload", paymentBatchPayUpload);
            }
        }

        rs.put("ret", 0);
        return JsonBizTool.genJson(ExRetEnum.SUCCESS, rs);
    }

    @RequestMapping(value = "paymentBatchPayUploadCompanyChange")
    @ResponseBody
    public String CompanyChange(String t_PIBU_Orgname, HttpServletRequest request,
                                HttpServletResponse response, Model model) {
        model.addAttribute("t_Org_name", t_PIBU_Orgname);
        String batch_PB_company = ShiroSessionUtil.getLoginSession().getCompany_name();
        Map<String, Object> rs = new HashMap<String, Object>();
        Map<String, Object> paramSearchMap = new HashMap<String, Object>();
        String t_O_OrgName = null;
        try {
            t_O_OrgName = URLDecoder.decode(t_PIBU_Orgname,"UTF-8");
        } catch (UnsupportedEncodingException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        OrganizationInfo organizationInfo = organizationInfoService.selectAgencyName(t_O_OrgName);
        paramSearchMap.put("batch_PB_company", batch_PB_company);
        paramSearchMap.put("t_FProd_OrgInfo", t_O_OrgName);
        paramSearchMap.put("t_FProd_CorpPool", organizationInfo.getT_O_Category());//添加元素
        List<FinanceProduct> FinanceProduct = financeProductService.findSearchList(paramSearchMap);
        model.addAttribute("FinanceProduct",FinanceProduct);
        rs.put("FinanceProduct", FinanceProduct);
        rs.put("ret", 0);
        return JsonBizTool.genJson(ExRetEnum.SUCCESS, rs);
    }

    // download Excel template  	
    @RequestMapping(value ="paymentBatchPayUploadAdd", method = {RequestMethod.GET})
    @ResponseBody
    public ResponseEntity<byte[]> downloadModel(@RequestParam("id") String id,HttpServletRequest req) throws InterruptedException {
//        RedisClient.getInstance().put("progress_"+id,0,60*60);
        ResponseEntity<byte[]> download = paymentBatchPayUploadService.download("files/paymentBatchPayUpload/template.xlsx", id, req);
//        RedisClient.getInstance().put("progress_"+id,100,60*60);
        return download;
    }

    @RequestMapping(value="paymentBatchPayUploadPullin")
    @ResponseBody
    public String upload(HttpServletRequest request,MultipartFile file,PaymentBatchPayUpload paymentBatchPayUpload,String personalInfoInputArea,String t_PIBU_Orgname,String t_FPROD_Name,
                         String payrollDate,String jobcat,Date EffectStartDate,Date EffectEndDate,Date TriggerTime,HttpServletResponse response, Model model) throws UnsupportedEncodingException {
        String CurrentCompany = ShiroSessionUtil.getLoginSession().getCompany_name();

        // Sring result = ps.readExcelFile(file);
        request.setCharacterEncoding("UTF-8");
        System.out.println("进入员工个人信息文件上传");
        System.out.println(file.getOriginalFilename());
        System.out.print(personalInfoInputArea);
        model.addAttribute("TriggerTime", TriggerTime);
        System.out.println(t_FPROD_Name);
        OrganizationInfo organizationInfo = organizationInfoService.selectAgencyName(t_PIBU_Orgname);
        Date date = new Date();//获取当前的日期
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        if(EffectStartDate == null){
            EffectStartDate = new Date();
        }
        if(EffectEndDate == null){
            EffectEndDate = EffectStartDate;
        }
        String datestr = Tool.PayId();
        String batch_PB_batchID = null;
        String FProd_name = null;
        Integer insertNum = 0;
        try {
            FProd_name = URLDecoder.decode(t_FPROD_Name, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        if (TriggerTime == null) {
            StringBuffer ss = new StringBuffer();
            if(personalInfoInputArea == null) {
                batch_PB_batchID = String.valueOf(ss.append(datestr.substring(0, (datestr.length()))).append("_").append(t_PIBU_Orgname).append("_").append(file.getOriginalFilename()).append("_").append(FProd_name).append("_").append("RT"));
            }else{
                batch_PB_batchID = String.valueOf(ss.append(datestr.substring(0, (datestr.length()))).append("_").append(t_PIBU_Orgname).append("_").append("temp").append("_").append(FProd_name).append("_").append("RT"));
            }
        } else {
            StringBuffer ss = new StringBuffer();
            if(personalInfoInputArea == null) {
                batch_PB_batchID = String.valueOf(ss.append(datestr.substring(0, (datestr.length()))).append("_").append(t_PIBU_Orgname).append("_").append(file.getOriginalFilename()).append("_").append(FProd_name).append("_").append("RT"));
            }else{
                batch_PB_batchID = String.valueOf(ss.append(datestr.substring(0, (datestr.length()))).append("_").append(t_PIBU_Orgname).append("_").append("temp").append("_").append(FProd_name).append("_").append("RT"));
            }
        }
        ArrayList<ArrayList<String>> row = new ArrayList<>();

        // err checking preparation
        List<String> personalErrInfo = new ArrayList<String>();
        StringBuffer errRowData =  new StringBuffer();
        Boolean dataChkOk = true;
        Map<String, Object> rs = new HashMap<String, Object>(); // err Msg

        if (personalInfoInputArea == null) {
            String filename = file.getOriginalFilename();
            Workbook workbook = null;
            try {
                InputStream Exlfile = file.getInputStream();
                if (judegExcelEdition(filename)) {
                    workbook = new XSSFWorkbook(Exlfile);
                } else {
                    workbook = new HSSFWorkbook(Exlfile);
                }
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

            //获取第一张工作表
            Sheet sheet = workbook.getSheetAt(0);
            //从第二行开始获取 getLastRowNum
            System.out.println("表总共多少行: " + sheet.getLastRowNum());
            System.out.println("实际共多少行: " + sheet.getPhysicalNumberOfRows());
            List<Map<String, Object>> cell = new ArrayList(); // get cell
            Readxlscells:
            for (int i = 0; i < sheet.getPhysicalNumberOfRows(); i++) {
                Map<String, Object> paramSQLmap = new HashMap<String, Object>();
                paramSQLmap.put("batch_PB_ID", Tool.uuid());
                paramSQLmap.put("batch_PB_batchID", batch_PB_batchID);
                paramSQLmap.put("batch_PB_company", t_PIBU_Orgname);
                paramSQLmap.put("batch_PB_vendorcompany", organizationInfo.getT_O_OrgPending());
                paramSQLmap.put("batch_PB_payrolldate", payrollDate);
                paramSQLmap.put("batch_PB_effectDate", EffectStartDate);
                paramSQLmap.put("batch_PB_endDate", EffectEndDate);
                paramSQLmap.put("batch_PB_flag", "debitpayment");
                paramSQLmap.put("batch_createtime", new Date());
                paramSQLmap.put("batch_PB_fprod", FProd_name);
                paramSQLmap.put("batch_creator", ShiroSessionUtil.getLoginSession().getUserName());
                //循环获取工作表的每一行
                Row sheetRow = sheet.getRow(i);
                //循环获取每一列
                //		ArrayList<String> cell = new ArrayList<>();
                for (int j = 0; j < sheetRow.getPhysicalNumberOfCells(); j++) {
                    if (j == 0 && sheetRow.getCell(j).getStringCellValue().contains(String.valueOf("员工"))) {
                        break;
                    }
                    //		将每一个单元格的值装入列集合
                    switch (j) {
                        case 0:
                            sheetRow.getCell(j).setCellType(XSSFCell.CELL_TYPE_STRING);
                            sheetRow.getCell(j).setCellType(Cell.CELL_TYPE_STRING);
                            System.out.println(sheetRow.getCell(j).getStringCellValue());
                            if (sheetRow.getCell(j).getStringCellValue() == null || sheetRow.getCell(j).getStringCellValue().equals("")) {
                                dataChkOk = false;
                                int rowNum = i + 1;
                                int colNum = j + 1;
                                errRowData.append(String.valueOf("姓名错误信息:")).append("第").append(rowNum).append("行-").append("第").append(colNum).append("列:").append("'").append(sheetRow.getCell(j).getStringCellValue()).append("'").append("-错误原因：").append(ExRetEnum.Pullin_UserNameErr);
                                rs.put("retMsg", errRowData);
                                personalErrInfo.add(errRowData.toString());
                                break Readxlscells;
                            } else {
                                paramSQLmap.put("batch_PB_Name", sheetRow.getCell(j).getStringCellValue().replaceAll(" ", ""));
                            }
                            break;
                        case 1:
                            sheetRow.getCell(j).setCellType(XSSFCell.CELL_TYPE_STRING);
                            sheetRow.getCell(j).setCellType(Cell.CELL_TYPE_STRING);
                            System.out.println(sheetRow.getCell(j).getStringCellValue());
                            boolean IsPIDcard = IdCardUtil.validate18Idcard(sheetRow.getCell(j).getStringCellValue());
                            boolean IDerr = sheetRow.getCell(j).getStringCellValue().contains("E");
                            if (sheetRow.getCell(j).getStringCellValue() == null || !IsPIDcard || IDerr) {
                                dataChkOk = false;
                                int rowNum = i + 1;
                                int colNum = j + 1;
                                errRowData = null;
                                rs.put("retMsg", "身份证错误信息: " + "第" + rowNum + "行-" + "第" + colNum + "列 :" + "'" + sheetRow.getCell(j).getStringCellValue().toUpperCase() + "'");
                                personalErrInfo.add(errRowData.toString());
                            } else {
                                paramSQLmap.put("batch_PB_PID", sheetRow.getCell(j).getStringCellValue().replaceAll(" ", ""));
                            }
                            break;
                        case 2:
                            sheetRow.getCell(j).setCellType(XSSFCell.CELL_TYPE_STRING);
                            sheetRow.getCell(j).setCellType(Cell.CELL_TYPE_STRING);
                            System.out.println(sheetRow.getCell(j).getStringCellValue());
                            boolean IsBankCard = Tool.checkBankCard(sheetRow.getCell(j).getStringCellValue());
                            boolean BankCarderr = sheetRow.getCell(j).getStringCellValue().contains("E");
                            if (sheetRow.getCell(j).getStringCellValue() == null || !IsBankCard || BankCarderr) {
                                dataChkOk = false;
                                int rowNum = i + 1;
                                int colNum = j + 1;
                                errRowData.append(String.valueOf("银行卡错误信息:")).append("第").append(rowNum).append("行-").append("第").append(colNum).append("列:").append("'").append(sheetRow.getCell(j).getStringCellValue()).append("'").append("-错误原因：").append(ExRetEnum.Pullin_UserDebitCardErr);
                                personalErrInfo.add(errRowData.toString());
                              return JsonBizTool.genJson(ExRetEnum.Pullin_UserDebitCardErr, rs);
                            } else {
                                paramSQLmap.put("batch_PB_creditCard", sheetRow.getCell(j).getStringCellValue().replaceAll(" ", "").toUpperCase());
                            }
                            break;
                        case 3:
                            sheetRow.getCell(j).setCellType(XSSFCell.CELL_TYPE_STRING);
                            sheetRow.getCell(j).setCellType(Cell.CELL_TYPE_STRING);
                            System.out.println(sheetRow.getCell(j).getStringCellValue());
                            boolean ISChineseMobile = Tool.isChinaPhoneLegal(sheetRow.getCell(j).getStringCellValue());
                            if (sheetRow.getCell(j).getStringCellValue() == null || !ISChineseMobile) {
                                int rowNum = i + 1;
                                int colNum = j + 1;
                                errRowData.append(String.valueOf("手机号错误信息:")).append("第").append(rowNum).append("行-").append("第").append(colNum).append("列:").append("'").append(sheetRow.getCell(j).getStringCellValue()).append("'").append("-错误原因：").append(ExRetEnum.Pullin_UserMobileErr);
                                personalErrInfo.add(errRowData.toString());
//                                return JsonBizTool.genJson(ExRetEnum.Pullin_UserMobileErr, rs);
                            } else {
                                paramSQLmap.put("batch_PB_mobile", sheetRow.getCell(j).getStringCellValue().replaceAll(" ", ""));
                            }
                            break;
                        case 4:
                            sheetRow.getCell(j).setCellType(XSSFCell.CELL_TYPE_STRING);
                            sheetRow.getCell(j).setCellType(Cell.CELL_TYPE_STRING);
                            System.out.println(sheetRow.getCell(j).getStringCellValue());
                            if (sheetRow.getCell(j).getStringCellValue() == null) {
                                dataChkOk = false;
                                int rowNum = i + 1;
                                int colNum = j + 1;
                                errRowData.append(String.valueOf("授额错误信息:")).append("第").append(rowNum).append("行-").append("第").append(colNum).append("列:").append("'").append(sheetRow.getCell(j).getStringCellValue()).append("'").append("-错误原因：").append(ExRetEnum.Pullin_UserCreditLineErr);
                                personalErrInfo.add(errRowData.toString());
//                                return JsonBizTool.genJson(ExRetEnum.Pullin_UserCreditLineErr, rs);
                            } else {
                                paramSQLmap.put("batch_PB_credit", new BigDecimal(sheetRow.getCell(j).getStringCellValue()));
                                paramSQLmap.put("batch_PB_balance", new BigDecimal(sheetRow.getCell(j).getStringCellValue()));
                            }
                            break;
                    }
                }
                //将装有每一列的集合装入大集合
                if (paramSQLmap.get("batch_PB_PID") != null) {
                    cell.add(paramSQLmap);
                }
            }
            if(dataChkOk) {
                insertNum = paymentBatchPayUploadService.insertCustomerMachineByBatch(cell);
            }else{
                rs.put("personalErrInfo",personalErrInfo);
                return JsonBizTool.genJson(ExRetEnum.Pullin_Fail, rs);
            }
        }else{
            //get data from personalInfoInputArea
            String personalInfoArray = null;
            try {
                personalInfoArray = URLDecoder.decode(personalInfoInputArea, "UTF-8");
            } catch (UnsupportedEncodingException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            //获取第一张工作表
            //从第二行开始获取 getLastRowNum
            List<Map<String, Object>> cell = new ArrayList();
            String[] personal_col_info = personalInfoArray.split(";");
            System.out.print(personal_col_info.length);

            for (int i = 0; i < personal_col_info.length; i++) {
                Map<String, Object> paramSQLmap = new HashMap<String, Object>();
                paramSQLmap.put("batch_PB_ID", Tool.uuid());
                paramSQLmap.put("batch_PB_batchID", batch_PB_batchID);
                paramSQLmap.put("batch_PB_company", t_PIBU_Orgname);
                paramSQLmap.put("batch_PB_vendorcompany", organizationInfo.getT_O_OrgPending());
                paramSQLmap.put("batch_PB_payrolldate", payrollDate);
                paramSQLmap.put("batch_PB_effectDate", EffectStartDate);
                paramSQLmap.put("batch_PB_endDate", EffectEndDate);
                paramSQLmap.put("batch_PB_flag", "debitpayment");
                paramSQLmap.put("batch_createtime", new Date());
                paramSQLmap.put("batch_PB_fprod", FProd_name);
                paramSQLmap.put("batch_creator", ShiroSessionUtil.getLoginSession().getUserName());
                //循环获取工作表的每一行
                String[] sheetRow = personal_col_info[i].split(",");
                //循环获取每一列
                //		ArrayList<String> cell = new ArrayList<>();
                if (sheetRow.length != 5) {
                    dataChkOk = false;
                    errRowData.append(String.valueOf("姓名错误信息:")).append("第").append(i+1).append("行-").append(sheetRow[i].trim().replace(" ","")).append("'").append("-错误原因：").append(ExRetEnum.PullinMutiText_Err);
//                    rs.put("retMsg", "该行数据错误: " + "第" + i+1 + "行-" + sheetRow[i].trim().replace(" ","") + "'");
                    personalErrInfo.add(errRowData.toString());
//                    return JsonBizTool.genJson(ExRetEnum.PullinMutiText_Err, rs);
                }
                for (int j = 0; j < sheetRow.length; j++) {
                    if (j == 0 && sheetRow[j].contains(String.valueOf("员工"))) {
                        break;
                    }
                    //		将每一个单元格的值装入列集合
                    switch (j) {
                        case 0:
                            System.out.println(sheetRow[j]);
                            if (sheetRow[j].trim().replace(" ","") == null || sheetRow[j].trim().replace(" ","") .equals("")) {
                                int rowNum = i + 1;
                                int colNum = j + 1;
                                dataChkOk = false;
                                errRowData.append(String.valueOf("姓名错误信息:")).append("第").append(rowNum).append("行-").append("第").append(colNum).append("列:").append("'").append(sheetRow[j].trim().replace(" ","")).append("'").append("-错误原因：").append(ExRetEnum.Pullin_UserNameErr);
                                personalErrInfo.add(errRowData.toString());
                                break;
                            } else {
                                paramSQLmap.put("batch_PB_Name", sheetRow[j].trim().replace(" ",""));
                            }
                            break;
                        case 1:
                            System.out.println(sheetRow[j]);
                            boolean IsPIDcard = IdCardUtil.validate18Idcard(sheetRow[j].trim().replace(" ",""));
                            boolean IDerr = sheetRow[j].trim().replace(" ","").contains("E");
                            if (sheetRow[j].trim().replace(" ","") == null || !IsPIDcard || IDerr) {
                                int rowNum = i + 1;
                                int colNum = j + 1;
                                dataChkOk = false;
                                errRowData.append(String.valueOf("身份证错误信息:")).append("第").append(rowNum).append("行-").append("第").append(colNum).append("列:").append("'").append(sheetRow[j].trim().replace(" ","").toUpperCase() ).append("'").append("-错误原因：").append(ExRetEnum.Pullin_UserIdErr);
//                                rs.put("retMsg", "身份证错误信息: " + "第" + rowNum + "行-" + "第" + colNum + "列 :" + "'" + sheetRow[j].trim().replace(" ","").toUpperCase() + "'");
                                personalErrInfo.add(errRowData.toString());
//                                return JsonBizTool.genJson(ExRetEnum.Pullin_UserIdErr, rs);
                            } else {
                                paramSQLmap.put("batch_PB_PID", sheetRow[j].trim().replace(" ",""));
                            }
                            break;
                        case 2:
                            System.out.println(sheetRow[j]);
                            boolean IsBankCard = Tool.checkBankCard(sheetRow[j].trim().replace(" ",""));
                            boolean BankCarderr = sheetRow[j].trim().replace(" ","").contains("E");
                            if (sheetRow[j].trim().replace(" ","") == null || !IsBankCard || BankCarderr) {
                                int rowNum = i + 1;
                                int colNum = j + 1;
                                dataChkOk = false;
                                errRowData.append(String.valueOf("银行卡错误信息:")).append("第").append(rowNum).append("行-").append("第").append(colNum).append("列:").append("'").append(sheetRow[j].trim().replace(" ","")).append("'").append("-错误原因：").append(ExRetEnum.Pullin_UserDebitCardErr);
                                personalErrInfo.add(errRowData.toString());
                            } else {
                                paramSQLmap.put("batch_PB_creditCard", sheetRow[j].trim().replace(" ","").toUpperCase());
                            }
                            break;
                        case 3:
                            System.out.println(sheetRow[j]);
                            boolean ISChineseMobile = Tool.isChinaPhoneLegal(sheetRow[j].trim().replace(" ",""));
                            if (sheetRow[j].trim().replace(" ","") == null || !ISChineseMobile) {
                                int rowNum = i + 1;
                                int colNum = j + 1;
                                dataChkOk = false;
                                errRowData.append(String.valueOf("手机号错误信息:")).append("第").append(rowNum).append("行-").append("第").append(colNum).append("列:").append("'").append(sheetRow[j].trim().replace(" ","")).append("'").append("-错误原因：").append(ExRetEnum.Pullin_UserMobileErr);
                                personalErrInfo.add(errRowData.toString());
                            } else {
                                paramSQLmap.put("batch_PB_mobile", sheetRow[j].trim().replace(" ",""));
                            }
                            break;
                        case 4:
                            System.out.println(sheetRow[j]);
                            if (sheetRow[j].trim().replace(" ","") == null) {
                                int rowNum = i + 1;
                                int colNum = j + 1;
                                dataChkOk = false;
                                errRowData.append(String.valueOf("授额错误信息:")).append("第").append(rowNum).append("行-").append("第").append(colNum).append("列:").append("'").append(sheetRow[j].trim().replace(" ","")).append("'").append("-错误原因：").append(ExRetEnum.Pullin_UserCreditLineErr);
                                personalErrInfo.add(errRowData.toString());
                            } else {
                                paramSQLmap.put("batch_PB_credit", new BigDecimal(sheetRow[j].trim().replace(" ","")));
                                paramSQLmap.put("batch_PB_balance", new BigDecimal(sheetRow[j].trim().replace(" ","")));
                            }
                            break;
                    }
                }
                //将装有每一列的集合装入大集合
                if (paramSQLmap.get("batch_PB_PID") != null) {
                    cell.add(paramSQLmap);
                }
            }
            if(dataChkOk) {
                insertNum = paymentBatchPayUploadService.insertCustomerMachineByBatch(cell);
            }else{
                rs.put("retMsg","插入失败，请检查相关记录并更正后再上传!");
                rs.put("personalErrInfo",personalErrInfo);
                return JsonBizTool.genJson(ExRetEnum.Pullin_Fail, rs);
            }
        }

        if(insertNum != 0){
            dataChkOk = true;
            //check dup debit card in upload batch
            List<PaymentBatchPayUpload> retcode = paymentBatchPayUploadService.duplicateDebitCardChk(batch_PB_batchID);
            String errRcsDupDebitCard = null;
            if (retcode.size() != 0 || !retcode.isEmpty()){
                StringBuffer errRecord = new StringBuffer();
                for (int i = 0; i < retcode.size(); i++) {
                    if (i == 0) {
                        errRecord.append("'").append(retcode.get(i).getBatch_payment_Name()).append("-").append(retcode.get(i).getBatch_payment_bankcard());
                    } else {
                        errRecord.append(",").append("'").append(retcode.get(i).getBatch_payment_Name()).append("-").append(retcode.get(i).getBatch_payment_bankcard()).append("'");
                    }
                }
                errRcsDupDebitCard = new String(errRecord);
            }

            if (errRcsDupDebitCard != null) {
                int deleteUpload = paymentBatchPayUploadService.deleteByPrimaryKey(batch_PB_batchID);
                dataChkOk = false;
                errRowData.append(String.valueOf("重复银行卡记录信息:")).append(errRcsDupDebitCard).append("-错误原因：").append(ExRetEnum.Pullin_FailDupDebitCardErr);
                personalErrInfo.add(errRowData.toString());
            }


            // check dup mobile with Manager Table
            List<PaymentBatchPayUpload> retcode1 = paymentBatchPayUploadService.duplicateMobileChkTmanager(batch_PB_batchID);
            String errRcsDupMobileMgr = null;
            if (retcode1.size() != 0 || !retcode1.isEmpty()){
                StringBuffer errRecord = new StringBuffer();
                for (int i = 0; i < retcode1.size(); i++) {
                    if (i == 0) {
                        errRecord.append("'").append(retcode1.get(i).getBatch_payment_Name()).append("-").append(retcode1.get(i).getBatch_payment_mobile());
                    } else {
                        errRecord.append(",").append("'").append(retcode1.get(i).getBatch_payment_Name()).append("-").append(retcode1.get(i).getBatch_payment_mobile()).append("'");
                    }
                }
                errRcsDupMobileMgr = new String(errRecord);
            }

            if (errRcsDupMobileMgr != null) {
                int deleteUpload = paymentBatchPayUploadService.deleteByPrimaryKey(batch_PB_batchID);
                dataChkOk = false;
                errRowData.append(String.valueOf("与系统主人员信息表重复手机号或一个身份证多个手机号记录信息:")).append(errRcsDupMobileMgr).append("-错误原因：").append(ExRetEnum.Pullin_FailDupMgrMobileErr);
                personalErrInfo.add(errRowData.toString());
            }

            // check dup Mobile with t_personal table
            List<PaymentBatchPayUpload> retcode2 = paymentBatchPayUploadService.duplicateMobileChkTperson(batch_PB_batchID);
            String errRcsDupMobilePer = null;
            if (retcode2.size() != 0 || !retcode2.isEmpty()){
                StringBuffer errRecord = new StringBuffer();
                for (int i = 0; i < retcode2.size(); i++) {
                    if (i == 0) {
                        errRecord.append("'").append(retcode2.get(i).getBatch_payment_Name()).append("-").append(retcode2.get(i).getBatch_payment_mobile());
                    } else {
                        errRecord.append(",").append("'").append(retcode2.get(i).getBatch_payment_Name()).append("-").append(retcode2.get(i).getBatch_payment_mobile()).append("'");
                    }
                }
                errRcsDupMobilePer = new String(errRecord);
            }

            if (errRcsDupMobilePer != null) {
                int deleteUpload = paymentBatchPayUploadService.deleteByPrimaryKey(batch_PB_batchID);
                dataChkOk = false;
                errRowData.append(String.valueOf("与系统个人信息表重复手机号或一个身份证多个手机号记录信息:")).append(errRcsDupMobilePer).append("-错误原因：").append(ExRetEnum.Pullin_FailDupPerMobileErr);
                personalErrInfo.add(errRowData.toString());
            }

            // check dup personal id with Manager table
            List<PaymentBatchPayUpload> retcode3 = paymentBatchPayUploadService.duplicatePIDChk(batch_PB_batchID);
            String errRcsDupPID = null;
            if (retcode3.size() != 0 || !retcode3.isEmpty()){
                StringBuffer errRecord = new StringBuffer();
                for (int i = 0; i < retcode3.size(); i++) {
                    if (i == 0) {
                        errRecord.append("'").append(retcode3.get(i).getBatch_payment_Name()).append("-").append(retcode3.get(i).getBatch_payment_PID());
                    } else {
                        errRecord.append(",").append("'").append(retcode3.get(i).getBatch_payment_Name()).append("-").append(retcode3.get(i).getBatch_payment_PID()).append("'");
                    }
                }
                errRcsDupPID = new String(errRecord);
            }

            if (errRcsDupPID != null) {
                int deleteUpload = paymentBatchPayUploadService.deleteByPrimaryKey(batch_PB_batchID);
                dataChkOk = false;
                errRowData.append(String.valueOf("与系统个人信息表重复身份证或一个手机号多个身份证记录信息:")).append(errRcsDupPID).append("-错误原因：").append(ExRetEnum.Pullin_FailDupPIDErr);
                personalErrInfo.add(errRowData.toString());
            }

            //check dup mobile in the batch uploaded
            List<PaymentBatchPayUpload> retcode4 = paymentBatchPayUploadService.checkDuplicateBatchUploadMobil(batch_PB_batchID);
            String errBatchDupMobile = null;
            if (retcode4.size() != 0 || !retcode4.isEmpty()){
                StringBuffer errRecord = new StringBuffer();
                for (int i = 0; i < retcode4.size(); i++) {
                    if (i == 0) {
                        errRecord.append("'").append(retcode4.get(i).getBatch_payment_Name()).append("-").append(retcode4.get(i).getBatch_payment_PID());
                    } else {
                        errRecord.append(",").append("'").append(retcode4.get(i).getBatch_payment_Name()).append("-").append(retcode4.get(i).getBatch_payment_PID()).append("'");
                    }
                }
                errBatchDupMobile = new String(errRecord);
            }

            if (errBatchDupMobile != null) {
                int deleteUpload = paymentBatchPayUploadService.deleteByPrimaryKey(batch_PB_batchID);
                dataChkOk = false;
                errRowData.append(String.valueOf("上传表中有重复手机号记录信息:")).append(errBatchDupMobile).append("-错误原因：").append(ExRetEnum.Pullin_FailDupBatchMobileErr);
                personalErrInfo.add(errRowData.toString());
            }

            //check dup personal id in the batch uploaded
            List<PaymentBatchPayUpload> retcode5 = paymentBatchPayUploadService.checkDuplicateBatchUploadPID(batch_PB_batchID);
            String errBatchDupPID = null;
            if (retcode5.size() != 0 || !retcode5.isEmpty()){
                StringBuffer errRecord = new StringBuffer();
                for (int i = 0; i < retcode5.size(); i++) {
                    if (i == 0) {
                        errRecord.append("'").append(retcode5.get(i).getBatch_payment_Name()).append("-").append(retcode5.get(i).getBatch_payment_PID());
                    } else {
                        errRecord.append(",").append("'").append(retcode5.get(i).getBatch_payment_Name()).append("-").append(retcode5.get(i).getBatch_payment_PID()).append("'");
                    }
                }
                errBatchDupPID = new String(errRecord);
            }

            if (errBatchDupPID != null) {
                int deleteUpload = paymentBatchPayUploadService.deleteByPrimaryKey(batch_PB_batchID);
                dataChkOk = false;
                errRowData.append(String.valueOf("上传表中有重复身份证记录信息:")).append(errBatchDupPID).append("-错误原因：").append(ExRetEnum.Pullin_FailDupBatchPIDErr);
                personalErrInfo.add(errRowData.toString());
            }

            //check dup debit card in the batch uploaded
            List<PaymentBatchPayUpload> retcode6 = paymentBatchPayUploadService.checkDuplicateBatchUploadDebitCard(batch_PB_batchID);
            String errBatchDupDebitCard = null;
            if (retcode6.size() != 0 || !retcode6.isEmpty()){
                StringBuffer errRecord = new StringBuffer();
                for (int i = 0; i < retcode6.size(); i++) {
                    if (i == 0) {
                        errRecord.append("'").append(retcode6.get(i).getBatch_payment_Name()).append("-").append(retcode6.get(i).getBatch_payment_PID());
                    } else {
                        errRecord.append(",").append("'").append(retcode6.get(i).getBatch_payment_Name()).append("-").append(retcode6.get(i).getBatch_payment_PID()).append("'");
                    }
                }
                errBatchDupDebitCard = new String(errRecord);
            }

            if (errBatchDupDebitCard != null) {
                int deleteUpload = paymentBatchPayUploadService.deleteByPrimaryKey(batch_PB_batchID);
                dataChkOk = false;
                errRowData.append(String.valueOf("上传表中有重复银行卡记录信息:")).append(errBatchDupDebitCard).append("-错误原因：").append(ExRetEnum.Pullin_FailDupDebitCardErr);
                personalErrInfo.add(errRowData.toString());
            }
        }


        StringBuffer ss = new StringBuffer();
        String succNum = null;
        if (dataChkOk) {
            succNum = String.valueOf(ss.append("总共成功上传条数: ").append(Integer.toString(insertNum)));
            rs.put("retMsg",succNum);
            return JsonBizTool.genJson(ExRetEnum.UPLOADSUCCESS, rs);
        }else{
            String uploadfaile = String.valueOf(ss.append("插入失败，请检查更正数据后再上传"));
            rs.put("retMsg",uploadfaile);
            return JsonBizTool.genJson(ExRetEnum.Pullin_Fail, rs);
        }

    }



    @RequestMapping(value = "personalInfoBatchUpdateSub")
    @ResponseBody
    public String PersonalInfoBatchUpdateSub(HttpServletRequest request,PaymentBatchPayUpload paymentBatchPayUpload,String personalInfoInputArea,
                                             String batch_PB_batchID,String t_PIBU_Orgname,String t_FPROD_Name,String payrollDate,String jobcat,Date EffectStartDate,Date EffectEndDate,
                                             Date TriggerTime,HttpServletResponse response, Model model) throws UnsupportedEncodingException{
        String CurrentCompany = ShiroSessionUtil.getLoginSession().getCompany_name();
        OrganizationInfo organizationInfo = organizationInfoService.selectAgencyName(CurrentCompany);
        model.addAttribute("TriggerTime", EffectStartDate);
        model.addAttribute("EffectStartDate", EffectStartDate);
        model.addAttribute("EffectEndDate", EffectEndDate);
        System.out.println(t_FPROD_Name);
        model.addAttribute("t_Org_name", t_PIBU_Orgname);
        Map<String, Object> rs = new HashMap<String, Object>();
        Map<String, Object> paramMap = new HashMap<String, Object>();
        Map<String, Object> paramSearchMap = new HashMap<String, Object>();
        String t_O_OrgName = null;
        try {
            t_O_OrgName = URLDecoder.decode(t_PIBU_Orgname,"UTF-8");
        } catch (UnsupportedEncodingException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        paramMap.put("batch_PB_batchID",paymentBatchPayUpload.getBatch_payment_batchID());
        paramMap.put("t_P_VendorCompany", organizationInfo.getT_O_OrgPending());//添加元素
        paramMap.put("t_P_Company",t_PIBU_Orgname);
        paramMap.put("batch_PB_payrolldate", payrollDate);
        paramMap.put("payrollDate",payrollDate);
        paramMap.put("jobcat",jobcat);
        //假设这是一个service类的片段
        int insertMangerNum = 0;
        int insertBatchPersonalNum = 0;

        try{
            insertMangerNum = paymentBatchPayUploadService.insertManagerInfo(paramMap);
            //出现异常
        } catch (Exception e) {
            e.printStackTrace();
            //设置手动回滚
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
        }

        if(insertMangerNum != 0){

            try{
                int insertTrManagerTxnNum = paymentBatchPayUploadService.insertRoleTrManagerInfoTxn(paramMap);
                //出现异常
            } catch (Exception e) {
                e.printStackTrace();
                //设置手动回滚
                TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            }

            try{
                int insertTrManagerTxnViewNum = paymentBatchPayUploadService.insertRoleTrManagerInfoTxnview(paramMap);
                //出现异常
            } catch (Exception e) {
                e.printStackTrace();
                //设置手动回滚
                TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            }

            try{
                insertBatchPersonalNum = paymentBatchPayUploadService.insertBatchPersonalInfo(paramMap);
                //出现异常
            } catch (Exception e) {
                e.printStackTrace();
                //设置手动回滚
                TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            }
        }

        try {
            batch_PB_batchID = URLDecoder.decode(paymentBatchPayUpload.getBatch_payment_batchID(), "UTF-8");
        } catch (UnsupportedEncodingException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        paramMap.put("batch_PB_batchID", batch_PB_batchID);
        paramMap.put("t_batch_EffectStartDate", EffectStartDate);
        paramMap.put("t_batch_EffectEndDate", EffectEndDate);
        paramMap.put("creator", ShiroSessionUtil.getLoginSession().getId());

        int insertSelectiveNum = paymentBatchPayUploadStatusService.insertSelective(paramMap);

        if(insertBatchPersonalNum == insertMangerNum && insertSelectiveNum != 0) {
            rs.put("insertMangerNum",insertMangerNum);
            rs.put("retMsg","成功添加一批: " + "其中新增人员数: " + insertMangerNum);
            return JsonBizTool.genJson(ExRetEnum.Pullin_SuccNewPer, rs);
        }else if(insertBatchPersonalNum != insertMangerNum && insertBatchPersonalNum != 0 ){
            String insertErr = null;
            StringBuffer sb = new StringBuffer();
            int deleteUpload = paymentBatchPayUploadService.deleteByPrimaryKey(batch_PB_batchID);
            insertErr = String.valueOf(sb.append("上传住信息表数:").append(insertMangerNum).append(",").append("上传个人信息表数:").append(insertBatchPersonalNum));
            rs.put("retData","-1");
            rs.put("retMsg","重复银行卡记录信息: " + insertErr);
            return JsonBizTool.genJson(ExRetEnum.SUCCESS, rs);
        }
        return "paymentBatchPayUpload/paymentBatchPayUploadNew";

    }



    // CLose PersonalBatchStatus	
    @RequestMapping(value ="personalInfoBatcStatusClose")
    @ResponseBody
    public String PersonalInfoBatcStatusClose(String t_PIBU_Orgname, HttpServletRequest request,String batch_PB_batchID,
                                              HttpServletResponse response, Model model) {
        model.addAttribute("t_Org_name", t_PIBU_Orgname);
        Map<String, Object> rs = new HashMap<String, Object>();
        Map<String, Object> paramMap = new HashMap<String, Object>();
        String t_O_OrgName = null;
        try {
            t_O_OrgName = URLDecoder.decode(t_PIBU_Orgname,"UTF-8");
        } catch (UnsupportedEncodingException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }


        int updateBatchPersonalStatusCloseNum = paymentBatchPayUploadService.updateBatchPersonalStatusClose(paramMap);

        rs.put("ret", 0);
        return JsonBizTool.genJson(ExRetEnum.SUCCESS, rs);
//    return "paymentBatchPayUpload/paymentBatchPayUploadNew";
    }


    @RequestMapping(value ="personalInfoBatcStatusOpenRefresh")  // 刷新额度 开启授额 工资项重新定义 做结算支付使用
    @ResponseBody
    public String personalInfoBatcStatusOpenRefresh(String t_PIBU_Orgname, HttpServletRequest request,String batch_PB_batchID,String operationType,
                                                    HttpServletResponse response, Model model) {
        model.addAttribute("t_Org_name", t_PIBU_Orgname);
        Map<String, Object> rs = new HashMap<String, Object>();
        Map<String, Object> paramMap = new HashMap<String, Object>();
        String t_O_OrgName = null;
        try {
            t_O_OrgName = URLDecoder.decode(t_PIBU_Orgname,"UTF-8");
        } catch (UnsupportedEncodingException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        int updateBatchPersonalTxnClearingNum = paymentBatchPayUploadService.updateBatchPersonalTxnClearing(paramMap);   // checking update record - ewallet clearing

        int deleteByRefreshBatchPersonalCreditNum = paymentBatchPayUploadService.deleteByRefreshBatchPersonalCredit(paramMap); // need to check, and remove from txn table which is ewallet salary related

        rs.put("ret", 0);
        return JsonBizTool.genJson(ExRetEnum.SUCCESS, rs);
    }

}