package review.infra;

import java.io.IOException;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Service;
import review.config.kafka.KafkaProcessor;
import review.domain.*;

@Service
public class ReviewsViewHandler {

    //<<< DDD / CQRS
    @Autowired
    private ReviewsRepository reviewsRepository;

    @StreamListener(KafkaProcessor.INPUT)
    public void whenReviewAdded_then_CREATE_1(
        @Payload ReviewAdded reviewAdded
    ) {
        try {
            if (!reviewAdded.validate()) return;

            // view 객체 생성
            Reviews reviews = new Reviews();
            // view 객체에 이벤트의 Value 를 set 함
            reviews.setId(reviewAdded.getId());
            reviews.setItemId(reviewAdded.getItemId());
            reviews.setRating(reviewAdded.getRating());
            reviews.setText(reviewAdded.getText());
            reviews.setUserId(reviewAdded.getUserId());
            reviews.setUserImg(reviewAdded.getUserImg());
            // view 레파지 토리에 save
            reviewsRepository.save(reviews);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @StreamListener(KafkaProcessor.INPUT)
    public void whenReviewUpdated_then_UPDATE_1(
        @Payload ReviewUpdated reviewUpdated
    ) {
        try {
            if (!reviewUpdated.validate()) return;
            // view 객체 조회
            Optional<Reviews> reviewsOptional = reviewsRepository.findById(
                reviewUpdated.getId()
            );

            if (reviewsOptional.isPresent()) {
                Reviews reviews = reviewsOptional.get();
                // view 객체에 이벤트의 eventDirectValue 를 set 함
                reviews.setRating(reviewUpdated.getRating());
                reviews.setText(reviewUpdated.getText());
                // view 레파지 토리에 save
                reviewsRepository.save(reviews);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @StreamListener(KafkaProcessor.INPUT)
    public void whenReviewDeleted_then_DELETE_1(
        @Payload ReviewDeleted reviewDeleted
    ) {
        try {
            if (!reviewDeleted.validate()) return;
            // view 레파지 토리에 삭제 쿼리
            reviewsRepository.deleteById(reviewDeleted.getId());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    //>>> DDD / CQRS
}