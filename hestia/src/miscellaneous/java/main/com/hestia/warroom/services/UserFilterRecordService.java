/*
 * Copyright (C) 2012 TopCoder Inc., All Rights Reserved.
 */
package com.hestia.warroom.services;

import com.hestia.warroom.entities.UserFilterRecord;
import com.hestia.warroom.services.dto.ServiceContext;
import com.hestia.warroom.services.dto.UserFilterRecordSearchCriteria;

/**
 * <p>
 * This interface defines service to manage user filter record. It extends GenericService.
 * </p>
 *
 * <p>
 * <strong>Thread Safety: </strong> Implementation should be thread safe after initialization.
 * </p>
 *
 * v1.1 - Export and Search Filter - added custom filter object
 * v1.2 - Hestia War Room Enhancements
 *      - added method retrieveLastSavedCriteria
 * @author zsudraco, sparemax, TCSASSEMBLER
 * @version 1.2
 */
public interface UserFilterRecordService extends GenericService<UserFilterRecord, UserFilterRecordSearchCriteria> {
    /**
     * This method retrieves the last saved user filter record. Null is returned if none is found.
     *
     * @param context
     *            the service context
     *
     * @return the last saved user filter record, or null if none is found.
     *
     * @throws IllegalArgumentException
     *             if context is null
     * @throws HestiaWarRoomNonUserException
     *             if context.user is null
     * @throws HestiaWarRoomPersistenceException
     *             if there is persistence error
     * @throws HestiaWarRoomServiceException
     *             if there is other error
     */
    public UserFilterRecord retrieveLastSavedUserFilter(ServiceContext context) throws HestiaWarRoomServiceException;

    /**
     * This method retrieves the last saved criteria record. Null is returned if none is found.
     *
     * @param context
     *            the service context
     *
     * @return the last saved criteria record, or null if none is found.
     *
     * @throws IllegalArgumentException
     *             if context is null
     * @throws HestiaWarRoomNonUserException
     *             if context.user is null
     * @throws HestiaWarRoomPersistenceException
     *             if there is persistence error
     * @throws HestiaWarRoomServiceException
     *             if there is other error
     * @since 1.2 Hestia War Room Enhancements
     */
    public UserFilterRecord retrieveLastSavedCriteria(ServiceContext context) throws HestiaWarRoomServiceException;
}
