package com.yangyao.controller;

import com.yangyao.dao.QRCodeDao;
import com.yangyao.pojo.QRCode;
import com.yangyao.pojo.QRCodeImpl;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class QRCodeController {
    @Autowired
    QRCodeDao qrCodes;
    @RequestMapping("/qrcodes")
    public String list(Model model) {
        model.addAttribute("qrcodelist", qrCodes.getAll());
        return "qrcode/list";
    }

    @GetMapping("/addqrcode")
    public String toAddPage() {
        return "qrcode/add";
    }

    @PostMapping("/addqrcode")
    public String addQRCode(QRCodeImpl qrcode) {
        qrCodes.addQRCode(qrcode);
        return "redirect:/qrcodes";
    }

    @GetMapping("/update/{id}")
    public String toUpdatePage(@PathVariable("id") Integer id, Model model) {
        model.addAttribute("editqrcode", qrCodes.getQRCode(id));
        return "qrcode/update";
    }

    @PostMapping("/updateqrcode")
    public String updateQRCode(QRCodeImpl qrcode) {
        //qrCodes.getQRCode(qrcode.getId()).setBarcodeText(qrcode.getBarcodeText());
        qrCodes.updateQRCode(qrcode.getId(), qrcode.getBarcodeText());
        return "redirect:/qrcodes";
    }

    @GetMapping("/delete/{id}")
    public String deleteQRCode(@PathVariable("id") Integer id) {
        qrCodes.deleteQRCode(id);
        return "redirect:/qrcodes";
    }

    @RequestMapping("/searchqrcode")
    public String toSearchPage(Model model, HttpSession session) {
        if (session.getAttribute("search") == null) {
            session.setAttribute("search", "");
        }
        if (session.getAttribute("search") == "") {
            model.addAttribute("search", session.getAttribute("search"));
            model.addAttribute("qrcodelist", qrCodes.getAll());
        } else {
            model.addAttribute("search", session.getAttribute("search"));
            model.addAttribute("qrcodelist", qrCodes.searchQRCode((String) session.getAttribute("search")));
            session.setAttribute("search", "");
        }
        return "qrcode/search";
    }

    @PostMapping("/searchqrcode")
    public String searchQRCode(String barcodeText, HttpSession session) {
        session.setAttribute("search", barcodeText);
        return "redirect:/searchqrcode";
    }
}
