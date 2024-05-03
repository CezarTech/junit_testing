package com.junit.cezartesting;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name="book_record")
@Builder
public class Book {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long bookId;
@NonNull
    private String name;
@NonNull
private String summary;

private int rating;
}
