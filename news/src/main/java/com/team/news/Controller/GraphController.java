package com.team.news.Controller;

import com.mongodb.BasicDBObject;
import com.mongodb.Block;
import com.mongodb.client.DistinctIterable;
import com.team.news.Form.SankeyForm;
import com.team.news.Form.SankeyFormAndDate;
import com.team.news.Repository.GraphRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.text.SimpleDateFormat;
import java.util.*;

@Controller
public class GraphController {


    private final GraphRepository graphRepository;

    @Autowired
    public GraphController(GraphRepository graphRepository) {
        this.graphRepository = graphRepository;
    }

    @ResponseBody
    @PostMapping("/sankey_major_post")
    public List<SankeyForm> sankey() {

        List<SankeyForm> list = new ArrayList<>();
        List<SankeyFormAndDate> sankeyFormAndDate;

        SimpleDateFormat date = new SimpleDateFormat("yyyy/MM/dd HH:mm ");
        Calendar cal = Calendar.getInstance();
        cal.add( Calendar.MINUTE, -30 );    // 1시간 이내
        String beforeTime = date.format(cal.getTime());

        sankeyFormAndDate = graphRepository.findSankeyFormAndDateByGroupAndDateGreaterThanEqual("major",beforeTime);
        list = sankeyFormAndDate.get(0).getSankeyitems();

        return list;
    }


    @ResponseBody
    @PostMapping("/sankey_minor_post")
    public List<SankeyForm> sankey_minor() {

        List<SankeyForm> list = new ArrayList<>();
        List<SankeyFormAndDate> sankeyFormAndDate;

        SimpleDateFormat date = new SimpleDateFormat("yyyy/MM/dd HH:mm");
        Calendar cal = Calendar.getInstance();
        cal.add( Calendar.MINUTE, -30 );    // 1시간 이내
        String beforeTime = date.format(cal.getTime());

        sankeyFormAndDate = graphRepository.findSankeyFormAndDateByGroupAndDateGreaterThanEqual("minor",beforeTime);
        list = sankeyFormAndDate.get(0).getSankeyitems();

        list.sort(Comparator.comparing(SankeyForm::getValue));

        return list.subList(list.size()-6,list.size());
    }

    @ResponseBody
    @PostMapping("/sankey_sports_post")
    public List<SankeyForm> sankey_sports() {

        List<SankeyForm> list = new ArrayList<>();
        List<SankeyFormAndDate> sankeyFormAndDate;

        SimpleDateFormat date = new SimpleDateFormat("yyyy/MM/dd HH:mm");
        Calendar cal = Calendar.getInstance();
        cal.add( Calendar.MINUTE, -30 );    // 1시간 이내
        String beforeTime = date.format(cal.getTime());

        sankeyFormAndDate = graphRepository.findSankeyFormAndDateByGroupAndDateGreaterThanEqual("sports",beforeTime);
        list = sankeyFormAndDate.get(0).getSankeyitems();

        return list;
    }

    @GetMapping("/sankey")
    public String main(Model model) {

        return "sankey";
    }
}
