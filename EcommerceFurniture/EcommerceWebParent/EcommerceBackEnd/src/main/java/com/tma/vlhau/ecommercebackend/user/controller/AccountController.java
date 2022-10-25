package com.tma.vlhau.ecommercebackend.user.controller;

import com.tma.vlhau.ecommercebackend.FileUploadUtil;
import com.tma.vlhau.ecommercebackend.security.UserDetailsImp;
import com.tma.vlhau.ecommercebackend.user.exception.UserNotFoundException;
import com.tma.vlhau.ecommercebackend.user.service.UserService;
import com.tma.vlhau.ecommercecommon.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;
import java.io.IOException;
import java.util.Objects;

@Controller
public class AccountController {

    @Autowired
    private UserService userService;

    @GetMapping("/account")
    public String viewProfile(@AuthenticationPrincipal UserDetailsImp loggedUser, Model model) throws UserNotFoundException {
        String email = loggedUser.getUsername();
        User user = userService.getUserByEmail(email);

        model.addAttribute("user", user);

        return "/user/account_form";
    }

    @PostMapping("/account/update")
    public String saveUser(@Valid User user, BindingResult bindingResult, RedirectAttributes redirectAttributes,
                           @AuthenticationPrincipal UserDetailsImp loggedUser,
                           @RequestParam("image") MultipartFile multipartFile, Model model) throws IOException,UserNotFoundException {
        if(user.getPassword() != ""){
            if (bindingResult.hasErrors()) {
                String email = loggedUser.getUsername();
                User UserDetailsImp = userService.getUserByEmail(email);
                model.addAttribute("userRole", UserDetailsImp.getRoles());
                return "/user/account_form";
            }
        }

        if (!multipartFile.isEmpty()) {
            String fileName = StringUtils.cleanPath(Objects.requireNonNull(multipartFile.getOriginalFilename()));
            user.setPhotos(fileName);
            User savedUser = userService.updateAccount(user);

            String uploadDir = FileUploadUtil.USER_DIR_NAME + savedUser.getId() + "/";

            FileUploadUtil.cleanDirectory(uploadDir);
            FileUploadUtil.saveFile(uploadDir, fileName, multipartFile);
        } else {
            if (user.getPhotos().isEmpty()) user.setPhotos(null);
            userService.updateAccount(user);
        }

        loggedUser.setFirstName(user.getFirstName());
        loggedUser.setLastName(user.getLastName());

        redirectAttributes.addFlashAttribute("message", "Your account details have been updated");

        return "redirect:/account";
    }

}
