package felipe.org.global_materials.dtos.request;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class VendaRequestDTO {

    private Long clienteId;

    @NotEmpty(message = "A venda deve conter ao menos um item")
    @Valid
    private List<ItemVendaRequestDTO> itens;
}