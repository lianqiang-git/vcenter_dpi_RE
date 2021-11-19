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

import com.ghca.vm.util.VmwareContext;
import com.vmware.vim25.*;

import java.util.List;


public class HostStorageSystemMo extends BaseMo {
    public HostStorageSystemMo(VmwareContext context, ManagedObjectReference morHostDatastore) {
        super(context, morHostDatastore);
    }

    public HostStorageSystemMo(VmwareContext context, String morType, String morValue) {
        super(context, morType, morValue);
    }

    public HostStorageDeviceInfo getStorageDeviceInfo() throws Exception {
        return (HostStorageDeviceInfo)_context.getVimClient().getDynamicProperty(_mor, "storageDeviceInfo");
    }

    public HostFileSystemVolumeInfo getHostFileSystemVolumeInfo() throws Exception {
        return _context.getVimClient().getDynamicProperty(_mor, "fileSystemVolumeInfo");
    }

    public void addInternetScsiStaticTargets(String iScsiHbaDevice, List<HostInternetScsiHbaStaticTarget> lstTargets) throws Exception {
        _context.getService().addInternetScsiStaticTargets(_mor, iScsiHbaDevice, lstTargets);
    }

    public void removeInternetScsiStaticTargets(String iScsiHbaDevice, List<HostInternetScsiHbaStaticTarget> lstTargets) throws Exception {
        _context.getService().removeInternetScsiStaticTargets(_mor, iScsiHbaDevice, lstTargets);
    }

    public void rescanHba(String iScsiHbaDevice) throws Exception {
        _context.getService().rescanHba(_mor, iScsiHbaDevice);
    }

    public void rescanVmfs() throws Exception {
        _context.getService().rescanVmfs(_mor);
    }

    public void mountVmfsVolume(String datastoreUuid) throws Exception {
        _context.getService().mountVmfsVolume(_mor, datastoreUuid);
    }

    public void unmountVmfsVolume(String datastoreUuid) throws Exception {
        _context.getService().unmountVmfsVolume(_mor, datastoreUuid);
    }

    public void unmapVmfsVolumeExTask(List<String> vmfsUuids) throws Exception {
        _context.getService().unmapVmfsVolumeExTask(_mor, vmfsUuids);
    }

    public void setMultipathLunPolicy(String lunId, HostMultipathInfoLogicalUnitPolicy hostMultipathInfoLogicalUnitPolicy ) throws Exception {
        _context.getService().setMultipathLunPolicy(_mor, lunId,hostMultipathInfoLogicalUnitPolicy);
    }

    public List<HostUnresolvedVmfsVolume> queryUnresolvedVmfsVolume() throws Exception {
        return _context.getService().queryUnresolvedVmfsVolume(_mor);
    }

    public void addInternetScsiSendTargets(String iScsiHbaDevice, List<HostInternetScsiHbaSendTarget> targets) throws HostConfigFaultFaultMsg, NotFoundFaultMsg, RuntimeFaultFaultMsg {
        _context.getService().addInternetScsiSendTargets(_mor, iScsiHbaDevice, targets);
    }
    public List<HostDiskPartitionInfo> retrieveDiskPartitionInfo(List<String> uids) throws Exception {
        return _context.getService().retrieveDiskPartitionInfo(_mor, uids);
    }

}
