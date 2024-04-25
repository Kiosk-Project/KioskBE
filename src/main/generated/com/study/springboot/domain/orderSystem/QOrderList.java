package com.study.springboot.domain.orderSystem;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QOrderList is a Querydsl query type for OrderList
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QOrderList extends EntityPathBase<OrderList> {

    private static final long serialVersionUID = 1663223278L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QOrderList orderList = new QOrderList("orderList");

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final com.study.springboot.domain.member.QMember member;

    public final ListPath<OrderItems, QOrderItems> orderItems = this.<OrderItems, QOrderItems>createList("orderItems", OrderItems.class, QOrderItems.class, PathInits.DIRECT2);

    public final DateTimePath<java.time.LocalDateTime> orderListTime = createDateTime("orderListTime", java.time.LocalDateTime.class);

    public final EnumPath<com.study.springboot.enumeration.OrderStatus> orderStatus = createEnum("orderStatus", com.study.springboot.enumeration.OrderStatus.class);

    public QOrderList(String variable) {
        this(OrderList.class, forVariable(variable), INITS);
    }

    public QOrderList(Path<? extends OrderList> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QOrderList(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QOrderList(PathMetadata metadata, PathInits inits) {
        this(OrderList.class, metadata, inits);
    }

    public QOrderList(Class<? extends OrderList> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.member = inits.isInitialized("member") ? new com.study.springboot.domain.member.QMember(forProperty("member")) : null;
    }

}
