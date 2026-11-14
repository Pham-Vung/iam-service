package vincent.pham.iamservice.entity;

import java.io.Serializable;

import jakarta.persistence.Column;
import jakarta.persistence.MappedSuperclass;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@MappedSuperclass
public abstract class BaseEntity extends BaseAudit implements Serializable {

    private static final long serialVersionUID = 1L;

    @Column(name = "is_deleted", nullable = false)
    private Boolean isDeleted = false;
}
