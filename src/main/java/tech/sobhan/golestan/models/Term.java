package tech.sobhan.golestan.models;

import lombok.*;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.util.Objects;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Term{
    @Id @GeneratedValue private Long id;
    private String title;
    private boolean open;

    @Override
    public Term clone(){
        return Term.builder().title(title).open(open).build();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Term term = (Term) o;
        return Objects.equals(title, term.title);
    }

    @Override
    public String toString() {
        return "Term(title="+title+", open="+open+")";
    }

    @Override
    public int hashCode() {
        return Objects.hash(title);
    }
}
