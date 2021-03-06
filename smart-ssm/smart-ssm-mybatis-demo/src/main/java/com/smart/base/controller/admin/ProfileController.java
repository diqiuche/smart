package com.smart.base.controller.admin;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.smart.base.service.UserService;
import com.smart.ssm.controller.BaseController;
import com.smart.ssm.model.JSONResult;
import com.smart.ssm.model.ResultCode;
import com.smart.ssm.validator.Validator;
import com.smart.ssm.validator.annotation.ValidateParam;
import com.smart.sso.rpc.AuthenticationRpcService;
import com.smart.sso.rpc.Permissionable;

/**
 * 管理员管理
 * 
 * @author Joe
 */
@Controller
@RequestMapping("/admin/profile")
public class ProfileController extends BaseController {

	@Resource
	private UserService userService;
	@Resource
	private AuthenticationRpcService authenticationRpcService;

	@RequestMapping(method = RequestMethod.GET)
	public String execute(Model model, HttpServletRequest request) {
		model.addAttribute("user", request.getSession().getAttribute(Permissionable.SESSION_PROFILE));
		return "/admin/profile";
	}

	@RequestMapping(value = "/savePassword", method = RequestMethod.POST)
	public @ResponseBody JSONResult save(
			@ValidateParam(name = "新密码", validators = { Validator.NOT_BLANK }) String newPassword,
			@ValidateParam(name = "确认密码", validators = { Validator.NOT_BLANK }) String confirmPassword,
			HttpServletRequest request) {
		if (newPassword.equals(confirmPassword)
				&& authenticationRpcService.updatePassword(
						request.getSession().getAttribute(Permissionable.SESSION_TOKEN).toString(), newPassword))
			return new JSONResult(ResultCode.SUCCESS, "修改成功");
		else
			return new JSONResult(ResultCode.ERROR, "修改失败");
	}
}