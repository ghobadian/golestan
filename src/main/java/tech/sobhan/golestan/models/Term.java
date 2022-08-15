package tech.sobhan.golestan.models;

import lombok.*;

import javax.persistence.*;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
@EqualsAndHashCode(of = "title")
@Entity
@Table(indexes = @Index(columnList = "title"))
public class Term{
    @Id 
    @GeneratedValue
    private Long id;
    private String title;
    private boolean open;
}
