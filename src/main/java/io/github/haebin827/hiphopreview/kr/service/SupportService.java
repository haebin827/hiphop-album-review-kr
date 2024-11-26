package io.github.haebin827.hiphopreview.kr.service;

import io.github.haebin827.hiphopreview.kr.dto.ArtistRequestDTO;
import io.github.haebin827.hiphopreview.kr.dto.FeedbackDTO;

public interface SupportService {

    void saveFeedback(FeedbackDTO reviewDTO, Integer userId);
    void saveArtistRequest(ArtistRequestDTO artistRequestDTO, Integer userId);
}
