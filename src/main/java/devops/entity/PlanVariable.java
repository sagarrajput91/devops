package devops.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "plan_variable")
public class PlanVariable {

    @Id
    @GeneratedValue(strategy =  GenerationType.AUTO)
    @Column(name = "plan_variable_id")
    Integer planVariableId;

    @Column(name = "plan_variable_key")
    String key;

    @Column(name = "plan_variable_value")
    String value;

    @ManyToOne
    @JoinColumn(name="plan_id", referencedColumnName = "plan_id", insertable = false)
    Plan plan;

}
