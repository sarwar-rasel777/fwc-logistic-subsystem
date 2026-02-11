package at.compax.reference.subsystem.fwclogistic.model.response;

import java.util.List;
import java.util.Map;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FwcOrderResponse {

  private OrderReadOnly readOnly;
  private String referenceNum;
  private String description;
  private String poNum;
  private String externalId;
  private String earliestShipDate;
  private String shipCancelDate;
  private String notes;
  private Double numUnits1;
  private Identifier unit1Identifier;
  private Double numUnits2;
  private Identifier unit2Identifier;
  private Double totalWeight;
  private Double totalVolume;
  private String billingCode;
  private String asnNumber;
  private Double upsServiceOptionCharge;
  private Double upsTransportationCharge;
  private Boolean addFreightToCod;
  private Boolean upsIsResidential;
  private Identifier exportChannelIdentifier;
  private String routePickupDate;
  private String shippingNotes;
  private String masterBillOfLadingId;
  private String invoiceNumber;
  private FulfillInvInfo fulfillInvInfo;
  private RoutingInfo routingInfo;
  private Billing billing;
  private ShipToInfo shipTo;
  private ShipToInfo soldTo;
  private ShipToInfo billTo;
  private List<SavedElement> savedElements;
  private ParcelOption parcelOption;

  @JsonProperty("_embedded")
  private OrderEmbeddedItems embedded;

  @Data
  @Builder
  @NoArgsConstructor
  @AllArgsConstructor
  public static class OrderReadOnly {
    private Long orderId;
    private Integer asnCandidate;
    private Integer routeCandidate;
    private Boolean fullyAllocated;
    private Integer importModuleId;
    private String exportModuleIds;
    private Boolean deferNotification;
    private Boolean isClosed;
    private String processDate;
    private String pickDoneDate;
    private String pickTicketPrintDate;
    private String packDoneDate;
    private Boolean labelsExported;
    private String invoiceExportedDate;
    private String invoiceDeliveredDate;
    private Integer loadedState;
    private String loadOutDoneDate;
    private String reallocatedAfterPickTicketDate;
    private Boolean routeSent;
    private String asnSentDate;
    private Boolean asnSent;
    private Boolean pkgsExported;
    private BatchIdentifier batchIdentifier;
    private List<Package> packages;
    private List<OutboundSerialNumber> outboundSerialNumbers;
    private Integer parcelLabelType;
    private CustomerIdentifier customerIdentifier;
    private Identifier facilityIdentifier;
    private Integer warehouseTransactionSourceType;
    private Integer transactionEntryType;
    private Identifier importChannelIdentifier;
    private String creationDate;
    private Identifier createdByIdentifier;
    private String lastModifiedDate;
    private Identifier lastModifiedByIdentifier;
    private Integer status;
  }

  @Data
  @Builder
  @NoArgsConstructor
  @AllArgsConstructor
  public static class Identifier {
    private String name;
    private Long id;
  }

  @Data
  @Builder
  @NoArgsConstructor
  @AllArgsConstructor
  public static class BatchIdentifier {
    private NameKey nameKey;
    private Long id;
  }

  @Data
  @Builder
  @NoArgsConstructor
  @AllArgsConstructor
  public static class NameKey {
    private CustomerIdentifier customerIdentifier;
    private String name;
    private String number;
  }

  @Data
  @Builder
  @NoArgsConstructor
  @AllArgsConstructor
  public static class CustomerIdentifier {
    private String externalId;
    private String name;
    private Long id;
  }

  @Data
  @Builder
  @NoArgsConstructor
  @AllArgsConstructor
  public static class Package {
    private Long packageId;
    private Integer packageTypeId;
    private Identifier packageDefIdentifier;
    private Double length;
    private Double width;
    private Double height;
    private Double weight;
    private Double codAmount;
    private Double insuredAmount;
    private String trackingNumber;
    private String description;
    private String createDate;
    private Boolean oversize;
    private Boolean cod;
    private Integer ucc128;
    private String cartonId;
    private String label;
    private List<PackageContent> packageContents;
  }

  @Data
  @Builder
  @NoArgsConstructor
  @AllArgsConstructor
  public static class PackageContent {
    private Long packageContentId;
    private Long packageId;
    private Long orderItemId;
    private Long receiveItemId;
    private Long orderItemPickExceptionId;
    private Double qty;
    private String lotNumber;
    private String serialNumber;
    private String expirationDate;
    private String createDate;
    private List<String> serialNumbers;
  }

  @Data
  @Builder
  @NoArgsConstructor
  @AllArgsConstructor
  public static class OutboundSerialNumber {
    private ItemIdentifierResponse itemIdentifier;
    private String qualifier;
    private List<String> serialNumbers;
  }

  @Data
  @Builder
  @NoArgsConstructor
  @AllArgsConstructor
  public static class ItemIdentifierResponse {
    private String sku;
    private Long id;
  }

  @Data
  @Builder
  @NoArgsConstructor
  @AllArgsConstructor
  public static class FulfillInvInfo {
    private Double fulfillInvShippingAndHandling;
    private Double fulfillInvTax;
    private String fulfillInvDiscountCode;
    private Double fulfillInvDiscountAmount;
    private String fulfillInvGiftMessage;
  }

  @Data
  @Builder
  @NoArgsConstructor
  @AllArgsConstructor
  public static class RoutingInfo {
    private Boolean isCod;
    private Boolean isInsurance;
    private Boolean requiresDeliveryConf;
    private Boolean requiresReturnReceipt;
    private String scacCode;
    private String carrier;
    private String mode;
    private String account;
    private String shipPointZip;
    private Identifier capacityTypeIdentifier;
    private String loadNumber;
    private String billOfLading;
    private String trackingNumber;
    private String trailerNumber;
    private String sealNumber;
    private String doorNumber;
    private String pickupDate;
  }

  @Data
  @Builder
  @NoArgsConstructor
  @AllArgsConstructor
  public static class Billing {
    private List<BillingCharge> billingCharges;
  }

  @Data
  @Builder
  @NoArgsConstructor
  @AllArgsConstructor
  public static class BillingCharge {
    private Integer chargeType;
    private Double subtotal;
    private List<ChargeDetail> details;
  }

  @Data
  @Builder
  @NoArgsConstructor
  @AllArgsConstructor
  public static class ChargeDetail {
    private Long warehouseTransactionPriceCalcId;
    private Double numUnits;
    private String chargeLabel;
    private String unitDescription;
    private Double chargePerUnit;
    private String glAcctNum;
    private String sku;
    private String ptItem;
    private String ptItemDescription;
    private String ptArAcct;
    private Boolean systemGenerated;
  }

  @Data
  @Builder
  @NoArgsConstructor
  @AllArgsConstructor
  public static class ShipToInfo {
    private RetailerInfo retailerInfo;
    private Integer sameAs;
    private Long retailerId;
    private Boolean isQuickLookup;
    private Long contactId;
    private String companyName;
    private String name;
    private String title;
    private String address1;
    private String address2;
    private String city;
    private String state;
    private String zip;
    private String country;
    private String phoneNumber;
    private String fax;
    private String emailAddress;
    private String dept;
    private String code;
    private Integer addressStatus;
  }

  @Data
  @Builder
  @NoArgsConstructor
  @AllArgsConstructor
  public static class RetailerInfo {
    private NameKey nameKey;
    private Long id;
  }

  @Data
  @Builder
  @NoArgsConstructor
  @AllArgsConstructor
  public static class SavedElement {
    private String name;
    private String value;
  }

  @Data
  @Builder
  @NoArgsConstructor
  @AllArgsConstructor
  public static class ParcelOption {
    private Long orderId;
    private String deliveryConfirmationType;
    private Integer deliveredDutyPaid;
    private Double dryIceWeight;
    private Double insuranceAmount;
    private Integer insuranceType;
    private String internationalContentsType;
    private String internationalNonDelivery;
    private Boolean residentialFlag;
    private Boolean saturdayDeliveryFlag;
  }

  @Data
  @Builder
  @NoArgsConstructor
  @AllArgsConstructor
  public static class OrderEmbeddedItems {
    @JsonProperty("http://api.3plCentral.com/rels/orders/item")
    private List<OrderItemResponse> items;
  }

  @Data
  @Builder
  @NoArgsConstructor
  @AllArgsConstructor
  public static class OrderItemResponse {
    private OrderItemReadOnly readOnly;
    private ItemIdentifierResponse itemIdentifier;
    private String qualifier;
    private String externalId;
    private Double qty;
    private Double secondaryQty;
    private String lotNumber;
    private String serialNumber;
    private String expirationDate;
    private Double weightImperial;
    private Double weightMetric;
    private String notes;
    private Double fulfillInvSalePrice;
    private Double fulfillInvDiscountPct;
    private Double fulfillInvDiscountAmt;
    private String fulfillInvNote;
    private List<SavedElement> savedElements;
    private List<ProposedAllocation> proposedAllocations;
    private List<OrderItemInPackage> orderItemInPackages;
  }

  @Data
  @Builder
  @NoArgsConstructor
  @AllArgsConstructor
  public static class OrderItemReadOnly {
    private Long orderItemId;
    private Boolean fullyAllocated;
    private Identifier unitIdentifier;
    private Identifier secondaryUnitIdentifier;
    private Double estimatedQty;
    private Double estimatedSecondaryQty;
    private Double originalPrimaryQty;
    private Boolean isOrderQtySecondary;
    private List<Allocation> allocations;
    private String bin;
    private String rowVersion;
  }

  @Data
  @Builder
  @NoArgsConstructor
  @AllArgsConstructor
  public static class Allocation {
    private Long receiveItemId;
    private Double qty;
    private Double properlyPickedPrimary;
    private Double properlyPickedSecondary;
    private Boolean loadedOut;
    private String rowVersion;
    private AllocationDetail detail;
  }

  @Data
  @Builder
  @NoArgsConstructor
  @AllArgsConstructor
  public static class AllocationDetail {
    private ItemTraits itemTraits;
    private LocationIdentifier locationIdentifier;
    private List<SavedElement> savedElements;
  }

  @Data
  @Builder
  @NoArgsConstructor
  @AllArgsConstructor
  public static class ItemTraits {
    private ItemIdentifierResponse itemIdentifier;
    private String qualifier;
    private String lotNumber;
    private String serialNumber;
    private String expirationDate;
    private PalletIdentifier palletIdentifier;
  }

  @Data
  @Builder
  @NoArgsConstructor
  @AllArgsConstructor
  public static class PalletIdentifier {
    private FacilityNameKey nameKey;
    private Long id;
  }

  @Data
  @Builder
  @NoArgsConstructor
  @AllArgsConstructor
  public static class LocationIdentifier {
    private FacilityNameKey nameKey;
    private Long id;
  }

  @Data
  @Builder
  @NoArgsConstructor
  @AllArgsConstructor
  public static class FacilityNameKey {
    private Identifier facilityIdentifier;
    private String name;
  }

  @Data
  @Builder
  @NoArgsConstructor
  @AllArgsConstructor
  public static class ProposedAllocation {
    private Long receiveItemId;
    private Double qty;
  }

  @Data
  @Builder
  @NoArgsConstructor
  @AllArgsConstructor
  public static class OrderItemInPackage {
    private Integer packageNumber;
    private Double quantity;
  }
}
