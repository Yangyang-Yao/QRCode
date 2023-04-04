package com.yangyao.controller;

import com.yangyao.dao.QRCodeDao;
import com.yangyao.pojo.QRCode;
import com.yangyao.pojo.QRCodeImpl;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
public class QRCodeController {
    @Autowired
    QRCodeDao qrCodes;
    @GetMapping("/qrcodes_sidebar")
    public String sideBarToList(Model model) {
        return "redirect:/qrcodes?sort_field=birth&sort_direction=desc";
    }
    @GetMapping("/qrcodes")
    public String toSortedList(@RequestParam(name = "sort_field") String sortField, @RequestParam(name = "sort_direction") String sortDirection, Model model) {
        model.addAttribute("qrcodelist", qrCodes.getAll(sortField, sortDirection));
        model.addAttribute("sortField", sortField);
        model.addAttribute("sortDirection", sortDirection);
        model.addAttribute("reverseSortDirection", sortDirection.equals("asc") ? "desc" : "asc");
        return "qrcode/list";
    }

    @GetMapping("/addqrcode")
    public String toAddPage(@RequestParam(name = "sort_field") String sortField, @RequestParam(name = "sort_direction") String sortDirection, Model model) {
        model.addAttribute("sortField", sortField);
        model.addAttribute("sortDirection", sortDirection);
        return "qrcode/add";
    }

    @PostMapping("/addqrcode")
    public String addQRCode(QRCodeImpl qrcode, String sortField, String sortDirection) {
        qrCodes.addQRCode(qrcode);
        return "redirect:/qrcodes?sort_field=" + sortField + "&sort_direction=" + sortDirection;
    }

    @GetMapping("/update/{id}")
    public String toUpdatePage(@PathVariable("id") Integer id, @RequestParam(name = "from") String from, @RequestParam(name = "pattern", defaultValue = "") String pattern, @RequestParam(name = "sort_field") String sortField, @RequestParam(name = "sort_direction") String sortDirection, Model model) {
        model.addAttribute("from", from);
        model.addAttribute("pattern", pattern);
        model.addAttribute("editqrcode", qrCodes.getQRCode(id));
        model.addAttribute("sortField", sortField);
        model.addAttribute("sortDirection", sortDirection);
        return "qrcode/update";
    }

    @PostMapping("/updateqrcode")
    public String updateQRCode(int id, String barcodeText, String from, String pattern, String sortField, String sortDirection) {
        //qrCodes.getQRCode(qrcode.getId()).setBarcodeText(qrcode.getBarcodeText());
        qrCodes.updateQRCode(id, barcodeText);
        if (from.equals("qrcodes")) {
            return "redirect:/" + from + "?sort_field=" + sortField + "&sort_direction=" + sortDirection;
        }
        return "redirect:/" + from + "?pattern=" + pattern + "&sort_field=" + sortField + "&sort_direction=" + sortDirection;
    }

    @GetMapping("/delete/{id}")
    public String deleteQRCode(@PathVariable("id") Integer id, @RequestParam(name = "from") String from, @RequestParam(name = "pattern", defaultValue = "") String pattern, @RequestParam(name = "sort_field") String sortField, @RequestParam(name = "sort_direction") String sortDirection) {
        qrCodes.deleteQRCode(id);
        if (from.equals("qrcodes")) {
            return "redirect:/" + from + "?sort_field=" + sortField + "&sort_direction=" + sortDirection;
        }
        return "redirect:/" + from + "?pattern=" + pattern + "&sort_field=" + sortField + "&sort_direction=" + sortDirection;
    }

    @GetMapping("/searchqrcode_sidebar")
    public String sideBarToSearchPage() {
        return "redirect:/searchqrcode?sort_field=birth&sort_direction=desc";
    }

    @GetMapping("/searchqrcode")
    public String toSearchPage(@RequestParam(name = "pattern", defaultValue = "") String pattern, @RequestParam(name = "sort_field") String sortField, @RequestParam(name = "sort_direction") String sortDirection, Model model) {
        model.addAttribute("pattern", pattern);
        model.addAttribute("sortField", sortField);
        model.addAttribute("sortDirection", sortDirection);
        model.addAttribute("reverseSortDirection", sortDirection.equals("asc") ? "desc" : "asc");
        if (pattern.equals("")) {
            model.addAttribute("qrcodelist", qrCodes.getAll(sortField, sortDirection));
        } else {
            model.addAttribute("qrcodelist", qrCodes.searchQRCode(pattern, sortField, sortDirection));
        }
        return "qrcode/search";
    }

    @PostMapping("/searchqrcode")
    public String searchQRCode(String pattern, String sortField, String sortDirection) {
        if (pattern.equals("")) {
            return "redirect:/searchqrcode?sort_field=" + sortField + "&sort_direction=" + sortDirection;
        }
        return "redirect:/searchqrcode?pattern=" + pattern + "&sort_field=" + sortField + "&sort_direction=" + sortDirection;
    }
}
