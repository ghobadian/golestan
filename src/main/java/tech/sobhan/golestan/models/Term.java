package tech.sobhan.golestan.models;

import lombok.*;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString(exclude = "id")
@EqualsAndHashCode(of = "title")
@Entity
public class Term{
    @Id 
    @GeneratedValue
    private Long id;
    private String title;
    private boolean open;
}
