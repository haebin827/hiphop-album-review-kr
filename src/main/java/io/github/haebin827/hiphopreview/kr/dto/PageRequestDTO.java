package io.github.haebin827.hiphopreview.kr.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class PageRequestDTO {

    @Builder.Default
    private int page = 1;

    @Builder.Default
    private int size = 10;

    private String type; // 검색의 종류: a, t, d

    private String keyword;

    @Builder.Default
    private String word = "";

    private Integer group; // menu group

    private String cat; // book category group

    private String link;

    private Boolean check;

    //radio button
    private String choose;

    public String[] getTypes() {
        if(type == null || type.isEmpty()) {
            return null;
        }
        return type.split("");
    }

    public Pageable getPageable(String...props) {
        return PageRequest.of(this.page -1, this.size, Sort.by(props).descending());
    }

    public String getLink() {

        if(link == null) {
            StringBuilder builder = new StringBuilder();

            builder.append("page=" + this.page);
            builder.append("&size=" + this.size);

            if (type != null && type.length() > 0) {
                builder.append("&type=" + type);
            }

            if (keyword != null && type != null && type.length() > 0) {
                try {
                    builder.append("&keyword=" + URLEncoder.encode(keyword, "UTF-8"));
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
            }

            if (word != null && !word.isEmpty()) {
                try {
                    builder.append("&word=" + URLEncoder.encode(word, "UTF-8"));
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
            }

            if (cat != null && !cat.isEmpty()) {
                try {
                    builder.append("&cat=" + URLEncoder.encode(cat, "UTF-8"));
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
            }

            if (group != null) {
                builder.append("&group=" + group);
            }

            if (check != null) {
                builder.append("&check=").append(check);
            }

            if (choose != null && !choose.isEmpty()) {
                builder.append("&choose=").append(choose);
            }

            link = builder.toString();
        }
        return link;
    }
}
