package review.domain;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;
import javax.persistence.*;
import lombok.Data;

//<<< EDA / CQRS
@Entity
@Table(name = "Reviews_table")
@Data
public class Reviews {

    @Id
    //@GeneratedValue(strategy=GenerationType.AUTO)
    private Long id;

    private String itemId;
    private Double rating;
    private String text;
    private String userId;
    @Lob
    private String userImg;
}