// Licensed to the Apache Software Foundation (ASF) under one
// or more contributor license agreements.  See the NOTICE file
// distributed with this work for additional information
// regarding copyright ownership.  The ASF licenses this file
// to you under the Apache License, Version 2.0 (the
// "License"); you may not use this file except in compliance
// with the License.  You may obtain a copy of the License at
//
//   http://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing,
// software distributed under the License is distributed on an
// "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
// KIND, either express or implied.  See the License for the
// specific language governing permissions and limitations
// under the License.
package com.ghca.vm.mo;

import com.ghca.vm.util.Pair;
import com.ghca.vm.util.VmwareContext;
import com.vmware.vim25.*;

import java.util.List;


/**
 * Interface to consolidate ESX(i) hosts and HA/FT clusters into a common interface used by CloudStack Hypervisor resources
 */
public interface VmwareHypervisorHost {
    VmwareContext getContext();

    ManagedObjectReference getMor();

    String getHyperHostName() throws Exception;

    ClusterDasConfigInfo getDasConfig() throws Exception;

    boolean isHAEnabled() throws Exception;

    void setRestartPriorityForVM(VirtualMachineMo vmMo, String priority) throws Exception;

    ManagedObjectReference getHyperHostDatacenter() throws Exception;

    ManagedObjectReference getHyperHostOwnerResourcePool() throws Exception;

    ManagedObjectReference getHyperHostCluster() throws Exception;

    boolean isHyperHostConnected() throws Exception;

    String getHyperHostDefaultGateway() throws Exception;

    List<VirtualMachineMo> listVmsOnHyperHost(String name) throws Exception;

    VirtualMachineMo findVmOnHyperHost(String name) throws Exception;

    VirtualMachineMo findVmOnPeerHyperHost(String name) throws Exception;

    boolean createVm(VirtualMachineConfigSpec vmSpec) throws Exception;

    boolean createBlankVm(String vmName, String vmInternalCSName, int cpuCount, int cpuSpeedMHz, int cpuReservedMHz, boolean limitCpuUse, int memoryMB,
                          int memoryReserveMB, String guestOsIdentifier, ManagedObjectReference morDs, boolean snapshotDirToParent,
                          Pair<String, String> controllerInfo, Boolean systemVm) throws Exception;

    ObjectContent[] getVmPropertiesOnHyperHost(String[] propertyPaths) throws Exception;

    ObjectContent[] getDatastorePropertiesOnHyperHost(String[] propertyPaths) throws Exception;

    ManagedObjectReference mountDatastore(boolean vmfsDatastore, String poolHostAddress, int poolHostPort, String poolPath, String poolUuid) throws Exception;

    void unmountDatastore(String poolUuid) throws Exception;

    ManagedObjectReference findDatastore(String poolUuid) throws Exception;

    ManagedObjectReference findDatastoreByName(String datastoreName) throws Exception;

    @Deprecated
    ManagedObjectReference findDatastoreByExportPath(String exportPath) throws Exception;

    ManagedObjectReference findMigrationTarget(VirtualMachineMo vmMo) throws Exception;

    VmwareHypervisorHostResourceSummary getHyperHostResourceSummary() throws Exception;

    VmwareHypervisorHostNetworkSummary getHyperHostNetworkSummary(String esxServiceConsolePort) throws Exception;

    ComputeResourceSummary getHyperHostHardwareSummary() throws Exception;

    LicenseAssignmentManagerMo getLicenseAssignmentManager() throws Exception;
    String getRecommendedDiskController(String guestOsId) throws Exception;
}
