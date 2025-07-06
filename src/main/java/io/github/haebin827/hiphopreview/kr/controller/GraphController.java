package io.github.haebin827.hiphopreview.kr.controller;

import lombok.extern.log4j.Log4j2;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.LinkedHashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/ratings")
@Log4j2
public class GraphController {

    @GetMapping
    public Map<String, Integer> getRatings() {

        Map<String, Integer> ratings = new LinkedHashMap<>();
        ratings.put("0", 2);  // 별점 0
        ratings.put("1", 4);  // 별점 1
        ratings.put("2", 8);  // 별점 2
        ratings.put("3", 15); // 별점 3
        ratings.put("4", 20); // 별점 4
        ratings.put("5", 10); // 별점 5
        return ratings;
    }
}

