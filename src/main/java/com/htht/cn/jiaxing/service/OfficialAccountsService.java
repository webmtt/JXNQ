package com.htht.cn.jiaxing.service;

import com.htht.cn.jiaxing.model.LandBlockModel;
import com.htht.cn.jiaxing.model.LandsQueryVO;
import com.htht.cn.jiaxing.model.OfficialAccountUserModel;
import com.htht.cn.jiaxing.model.User;
import com.htht.cn.jiaxing.model.dto.OfficialAccountUserDTO;

/**
 * @author zw
 */
public interface OfficialAccountsService {
    public String landBind(LandBlockModel landBindModel, User user);

    String landEdit(LandBlockModel landBindModel);

	LandsQueryVO queryLands(Long userid) throws Exception;

    String register(OfficialAccountUserModel user);

    Object login(OfficialAccountUserModel user);

    OfficialAccountUserDTO findUserById(Long userId);

    String landDel(String landId);
}

