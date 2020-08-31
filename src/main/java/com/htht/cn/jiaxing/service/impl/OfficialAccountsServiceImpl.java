package com.htht.cn.jiaxing.service.impl;

import com.htht.cn.jiaxing.mapper.LandInfoMapper;
import com.htht.cn.jiaxing.mapper.OfficialAccontMapper;
import com.htht.cn.jiaxing.model.LandBlockModel;
import com.htht.cn.jiaxing.model.LandsQueryVO;
import com.htht.cn.jiaxing.model.OfficialAccountUserModel;
import com.htht.cn.jiaxing.model.User;
import com.htht.cn.jiaxing.model.dto.LandInfoDTO;
import com.htht.cn.jiaxing.model.dto.OfficialAccountUserDTO;
import com.htht.cn.jiaxing.service.OfficialAccountsService;
import com.htht.cn.jiaxing.utils.AESUtils;
import com.htht.cn.jiaxing.utils.ValidateUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.UUID;


/**
 * @author zw
 */
@Service
@Slf4j
public class OfficialAccountsServiceImpl implements OfficialAccountsService{


    @Autowired
    LandInfoMapper landInfoMapper;

    @Autowired
    OfficialAccontMapper officialAccontMapper;




    @Override
    @Transactional(rollbackFor = Exception.class)
    public String landBind(LandBlockModel landBlockModel, User user){
        List<LandBlockModel> landBlockModels = landInfoMapper.selectByFarmer(user.getUser_id());
        String id = UUID.randomUUID().toString().replaceAll("-", "");

        LandInfoDTO landInfoDTO = new LandInfoDTO();
        landInfoDTO.setId(id);
        landInfoDTO.setAddress(landBlockModel.getAddress());
        landInfoDTO.setCity(landBlockModel.getCity());
        landInfoDTO.setDistrict(landBlockModel.getDistrict());
        landInfoDTO.setArea(landBlockModel.getArea());
        landInfoDTO.setCrop(landBlockModel.getCrops());
        landInfoDTO.setBinder(user.getUser_id());
        if(null != landBlockModels && landBlockModels.size() >= 3) {
            return "地块已超过三个,不得增加";
        }
        landInfoMapper.insertOne(landInfoDTO);
        return "";
//
    }

    @Override
    public String landEdit(LandBlockModel landBindModel) {
        String id = landBindModel.getId();
        if(StringUtils.isEmpty(id)) {
            return "地块编号不得为空";
        }
        LandInfoDTO landInfoDTO = new LandInfoDTO();
        landInfoDTO.setCity(landBindModel.getCity());
        landInfoDTO.setDistrict(landBindModel.getDistrict());
        landInfoDTO.setAddress(landBindModel.getAddress());
        landInfoDTO.setCrop(landBindModel.getCrops());
        landInfoDTO.setArea(landBindModel.getArea());
        landInfoDTO.setId(landBindModel.getId());
        landInfoMapper.update(landInfoDTO);
        return null;
    }

    @Override
    public LandsQueryVO queryLands(Long userid) throws Exception {
        OfficialAccountUserDTO user = officialAccontMapper.selectById(userid);
        if(user == null) {
            throw new Exception("未找到指定的用户");
        }
        LandsQueryVO landsQueryVO = new LandsQueryVO();
        landsQueryVO.setAccountName(user.getUserName());
        landsQueryVO.setMobile(shouJiHaoTuoMing(user.getMobile()));
        List<LandBlockModel> lands =  landInfoMapper.selectByFarmer(userid);
        if(lands != null && lands.size()>0) {

            landsQueryVO.setBlocks(lands);
        }
        return landsQueryVO;
    }

    @Override
    public String register(OfficialAccountUserModel user) {
        boolean isLleagal = checkName(user.getUserName());
        if(! isLleagal) {
            return "用户名只允许2-16位的中文,英文,数字或下划线";
        }
        boolean isMobile = ValidateUtils.isMobile(user.getMobile());
        if(!isMobile) {
            return "请输入正确的手机号";
        }
        String password = user.getPassword();
        boolean b = checkPassword(password);
        if(!b) {
            return "密码最少6位,包括至少1个大写字母,1个小写字母";
        }
        try {
            //再加密存储
            password = AESUtils.encrypt(password);
            user.setPassword(password);

        }catch (Exception e) {
            log.error("密码加密错误",e);
            return "密码加密错误";
        }
        String mobile =  AESUtils.encrypt(user.getMobile());
        user.setMobile(mobile);
        OfficialAccountUserDTO userByMobile = officialAccontMapper.findUserByName(user.getUserName());
        if(null != userByMobile) {
            return "该用户名已注册,请更换姓名在试";
        }
        officialAccontMapper.insertOne(user);
        return "";
    }

    private boolean checkPassword(String password) {
        if(StringUtils.isEmpty(password)) {
            return false;
        }
        String s = "^.*(?=.{6,})(?=.*[A-Z])(?=.*[a-z]).*$";
        boolean matches = password.matches(s);
        return matches;
    }

    public static void main(String[] args) {
        String s = "^.*(?=.{6,})(?=.*[A-Z])(?=.*[a-z]).*$";
        boolean matches = "asdfasdfA".matches(s);
        System.out.println(matches);
    }

    //只允许中文 英文 数字
    private boolean checkName(String userName) {
            if(StringUtils.isEmpty(userName)) {
                return false;
            }
            Boolean flag = true;
//            String reg1 = "[\\u4e00-\\u9fa5]";
//            String reg2 = "[a-zA-Z]";
//            String reg3 = "[0-9]";
            String regExp = "^[a-zA-Z0-9_\\u4e00-\\u9fa5]{2,16}$";

//            char[] charArray = userName.toCharArray();
//            for (char c : charArray) {
//                String str = String.valueOf(c);
//                if(!str.matches(reg1) && !str.matches(reg2) && !str.matches(reg3)) {
//                    flag = false;
//                    break;
//                }
//            }
            flag = userName.matches(regExp);


            return flag;
    }

    @Override
    public Object login(OfficialAccountUserModel user) {
        String password = user.getPassword();
        OfficialAccountUserDTO userByMobile = officialAccontMapper.findUserByName(user.getUserName());
        if(null == userByMobile) {
            return "账号或密码错误";
        }
        String decrypt = AESUtils.decrypt(userByMobile.getPassword());
        if(!password.equals(decrypt)) {
            //TODO 重试次数
            return "账号或密码错误";
        }

        return userByMobile;
    }

    @Override
    public OfficialAccountUserDTO findUserById(Long userId) {
        OfficialAccountUserDTO officialAccountUserDTO = officialAccontMapper.selectById(userId);
        String mobile = officialAccountUserDTO.getMobile();
        mobile = shouJiHaoTuoMing(mobile);
        officialAccountUserDTO.setMobile(mobile);
        return officialAccountUserDTO;
    }

    @Override
    public String landDel(String landId) {
        if(StringUtils.isEmpty(landId)) {
            return "地块id不得为空";
        }
        landInfoMapper.delete(landId);
        return null;
    }

    //手机号脱敏
    private String shouJiHaoTuoMing(String mobile) {
        String decrypt = AESUtils.decrypt(mobile);
        //手机号11位
        String prefix = decrypt.substring(0, 3);
        String suffix = decrypt.substring(7,11);
        return prefix+"****"+suffix;
    }


}
