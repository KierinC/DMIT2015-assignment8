package dmit2015.repository;

import dmit2015.entity.Bill;
import dmit2015.entity.BillPayment;

import dmit2015.security.BillPaymentRepositorySecurityInterceptor;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.interceptor.Interceptors;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import jakarta.security.enterprise.SecurityContext;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@ApplicationScoped
@Transactional
@Interceptors({BillPaymentRepositorySecurityInterceptor.class})
public class BillPaymentRepository {

    @PersistenceContext
    private EntityManager _entityManager;

    @Inject
    private SecurityContext _securityContext;

    public Optional<BillPayment> findOneById(Long id) {
        Optional<BillPayment> optionalBillPayment = Optional.empty();
        try {
            BillPayment querySingleResult = _entityManager.find(BillPayment.class, id);
            if (querySingleResult != null) {
                optionalBillPayment = Optional.of(querySingleResult);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return optionalBillPayment;
    }

    /**
     * Return all BillPayment for the current user
     * @return list of BillPayment for the current user
     */
    public List<BillPayment> findAll() {
        List<BillPayment> payments;

        if (_securityContext.getCallerPrincipal().getName().equals("anonymous")) {
            throw new RuntimeException("You must be authenticated to access this method.");
        } else if (_securityContext.isCallerInRole("Finance")) {
            payments = _entityManager.createQuery(
                    "SELECT bp FROM BillPayment bp WHERE bp.username = :username ORDER BY bp.paymentDate DESC "
                    , BillPayment.class)
                    .setParameter("username", _securityContext.getCallerPrincipal().getName())
                    .getResultList();
        } else if (_securityContext.isCallerInRole("Accounting") || _securityContext.isCallerInRole("Executive")) {
            payments = _entityManager.createQuery(
                    "SELECT bp FROM BillPayment bp ORDER BY bp.paymentDate DESC "
                    , BillPayment.class)
                    .getResultList();
        } else {
            payments = new ArrayList<>();
        }

        payments =  _entityManager.createQuery(
                "SELECT bp FROM BillPayment bp ORDER BY bp.paymentDate DESC "
                , BillPayment.class)
                .getResultList();

        return payments;
    }

    public List<BillPayment> findAllByBillId(Long billId) {
        return _entityManager.createQuery(
                "SELECT bp "
                        + " FROM BillPayment bp "
                        + " WHERE bp.billToPay.id = :billId "
                        + " ORDER BY bp.paymentDate DESC "
                , BillPayment.class)
                .setParameter("billId", billId)
                .getResultList();
    }

    public void create(BillPayment newBillPayment) {
        // Subtract the BillPayment paymentAmount from the Bill paymentBalance
        Bill existingBill = newBillPayment.getBillToPay();
        existingBill.setPaymentBalance(existingBill.getPaymentBalance().subtract(newBillPayment.getPaymentAmount()));
        // Set the payment to the current date
        newBillPayment.setPaymentDate(LocalDate.now());

        // Get the username of the current user
        String username = _securityContext.getCallerPrincipal().getName();
        // Set the username of the current user
        newBillPayment.setUsername(username);
        // Save the newBillPayment
        _entityManager.persist(newBillPayment);
        // Update the existingBill
        _entityManager.merge(existingBill);
    }

    public void update(BillPayment updatedBillPayment) {
        Optional<BillPayment> optionalBillPayment = findOneById(updatedBillPayment.getId());
        if (optionalBillPayment.isPresent()) {
            BillPayment existingBillPayment = optionalBillPayment.get();

            // Update the amountBalance on the Bill by adding the previous paymentAmount
            // and subtract the new paymentAmount
            Bill existingBill = existingBillPayment.getBillToPay();
            BigDecimal previousPaymentAmount = existingBillPayment.getPaymentAmount();
            BigDecimal newPaymentAmount = updatedBillPayment.getPaymentAmount();
            BigDecimal paymentAmountChange = newPaymentAmount.subtract(previousPaymentAmount);
            BigDecimal newAmountBalance = existingBill.getPaymentBalance().subtract(paymentAmountChange);
            existingBill.setPaymentBalance(newAmountBalance);
            _entityManager.merge(existingBill);

            existingBillPayment.setPaymentAmount(updatedBillPayment.getPaymentAmount());
            existingBillPayment.setPaymentDate(updatedBillPayment.getPaymentDate());
            existingBillPayment.setVersion(updatedBillPayment.getVersion());

            // Update the existingBillPayment
            _entityManager.merge(existingBillPayment);
            _entityManager.flush();
        }
    }

    public void delete(Long id) {
        Optional<BillPayment> optionalBillPayment = findOneById(id);
        if (optionalBillPayment.isPresent()) {
            BillPayment existingBillPayment = optionalBillPayment.get();

            // Add the paymentAmount from the Bill
            Bill existingBill = existingBillPayment.getBillToPay();
            existingBill.setPaymentBalance(existingBill.getPaymentBalance().add(existingBillPayment.getPaymentAmount()));
            _entityManager.merge(existingBill);
            // Remove the Bill
            _entityManager.remove(existingBillPayment);
        }
    }
}