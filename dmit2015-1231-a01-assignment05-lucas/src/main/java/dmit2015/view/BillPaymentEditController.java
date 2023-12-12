package dmit2015.view;

import dmit2015.entity.BillPayment;
import dmit2015.repository.BillPaymentRepository;
import lombok.Getter;
import lombok.Setter;
import org.omnifaces.util.Faces;
import org.omnifaces.util.Messages;

import jakarta.annotation.PostConstruct;
import jakarta.faces.annotation.ManagedProperty;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import java.io.Serializable;
import java.util.Optional;

@Named("currentBillPaymentEditController")
@ViewScoped
public class BillPaymentEditController implements Serializable {

    @Inject
    private BillPaymentRepository _billpaymentRepository;

    @Inject
    @ManagedProperty("#{param.editId}")
    @Getter
    @Setter
    private Long editId;

    @Getter
    private BillPayment existingBillPayment;

    @PostConstruct
    public void init() {
        if (!Faces.isPostback()) {
            Optional<BillPayment> optionalBillPayment = _billpaymentRepository.findOneById(editId);
            if (optionalBillPayment.isPresent()) {
                existingBillPayment = optionalBillPayment.get();
            }
        }
    }

    public String onUpdate() {
        String nextPage = "";
        try {
            _billpaymentRepository.update(existingBillPayment);
            Messages.addFlashGlobalInfo("Update was successful.");
            nextPage = "index?faces-redirect=true";
        } catch (Exception e) {
            e.printStackTrace();
            Messages.addGlobalError("Update was not successful. {0}", e.getMessage());
        }
        return nextPage;
    }

    public String onDelete() {
        String nextPage = "";
        try {
            _billpaymentRepository.delete(existingBillPayment.getId());
            Messages.addFlashGlobalInfo("Delete was successful.");
            nextPage = "index?faces-redirect=true";
        } catch (Exception e) {
            e.printStackTrace();
            Messages.addGlobalError("Delete not successful. {0}", e.getMessage());
        }
        return nextPage;
    }
}