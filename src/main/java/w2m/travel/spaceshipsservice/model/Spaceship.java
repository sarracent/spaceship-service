package w2m.travel.spaceshipsservice.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Spaceship {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @NotNull(message = "name is required")
    @NotBlank(message = "name cannot be empty")
    private String name;

    @NotNull(message = "model is required")
    @NotBlank(message = "model cannot be empty")
    private String model;

    @NotNull(message = "color is required")
    @NotBlank(message = "color cannot be empty")
    private String color;

    @NotNull(message = "manufacturing Date is required")
    private LocalDate manufacturingDate;

    @OneToMany(mappedBy = "spaceship")
    private List<Serie> series;
}

