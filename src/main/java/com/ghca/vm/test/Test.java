package com.ghca.vm.test;

import com.ghca.vm.mo.*;
import com.ghca.vm.util.Pair;
import com.ghca.vm.util.VmwareContextFactory;
import com.ghca.vm.util.VmwareContext;
import com.vmware.vim25.*;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;

public class Test {

    public static void main(String[] args) throws Exception {

        try {

            //VmwareContext vmwareContext = VmwareContextFactory.getContext("10.143.132.249","administrator@vsphere.local","Pbu4@123");
            VmwareContext vmwareContext = VmwareContextFactory.getContext("10.143.133.205", "443", "administrator@vsphere.local", "Pbu4@123");
            DatacenterMo dcMo = new DatacenterMo(vmwareContext, "Datacenter");
            HostMo HostMo = new HostMo(vmwareContext, dcMo.findHost("10.143.133.17"));
            HostDatastoreSystemMo HostDatastoreSystemMo = HostMo.getHostDatastoreSystemMo();
            DatastoreMo dsMo = new DatastoreMo(vmwareContext, dcMo.findDatastore("zg701_150002"));
            List<VmfsDatastoreOption> vmfsDatastoreOptions = HostDatastoreSystemMo.queryVmfsDatastoreExpandOptions(dsMo);

            //HostStorageSystemMO hostStorageSystemMo = HostMo.getHostStorageSystemMo();
            //hostStorageSystemMo.rescanVmfs();

            //List<String> uuids = new ArrayList<>();
            //String deviceName = "/vmfs/devices/disks/naa.67c1cf1100589345343ce9aa000000cf";
            //uuids.add(deviceName);
            //List<HostDiskPartitionInfo> hostDiskPartitionInfos = hostStorageSystemMo.retrieveDiskPartitionInfo(uuids);
            //HostDiskPartitionInfo hostDiskPartitionInfo = hostDiskPartitionInfos.get(0);
            //long currentEndSector = hostDiskPartitionInfo.getSpec().getPartition().get(0).getEndSector();
            HostMo = new HostMo(vmwareContext, dcMo.findHost("10.143.133.196"));
            //HostMo.getHostStorageSystemMo().rescanVmfs();
            List<DatastoreHostMount> HostMounts = dsMo.getHostMounts();
            // 查询目前未挂载的卷
            for (HostFileSystemMountInfo mount : HostMo.getHostStorageSystemMo()
                    .getHostFileSystemVolumeInfo()
                    .getMountInfo()) {
                if (mount.getVolume() instanceof HostVmfsVolume && "zg701_150002".equals(mount.getVolume().getName())) {
                    HostVmfsVolume volume = (HostVmfsVolume) mount.getVolume();

                    // 挂载卷
                   HostMo.getHostStorageSystemMo().mountVmfsVolume(volume.getUuid());
                }
            }


            VmfsDatastoreInfo vmfsDatastoreInfo = dsMo.getVmfsDatastoreInfo();
            List<DatastoreHostMount> HostMounts2 = dsMo.getHostMounts();
            List<String> mounthostids = new ArrayList<>();
            if (!CollectionUtils.isEmpty(HostMounts)) {
                for (DatastoreHostMount dhm : HostMounts) {
                    if (dhm.getMountInfo() != null && dhm.getMountInfo().isMounted()) {
                        mounthostids.add(dhm.getKey().getValue());
                    }
                }
            }
            RootFsMo RootFsMo = new RootFsMo(vmwareContext, vmwareContext.getRootFolder());
            List<Pair<ManagedObjectReference, String>> hosts = RootFsMo.getAllHostOnRootFs();
            Boolean isDelete = false;
            if (hosts != null && hosts.size() > 0) {
                for (Pair<ManagedObjectReference, String> host : hosts) {
                    HostMo host1 = new HostMo(vmwareContext, host.first());
                    if (mounthostids.contains(host1.getMor().getValue())) {
                        HostDatastoreSystemMo hdsMo = host1.getHostDatastoreSystemMo();
                        isDelete = hdsMo.deleteDatastore("LQ_0608");
                        break;
                    }
                }
            }

            String datastoreobjectid = "urn:vmomi:Datastore:datastore-17187:674908e5-ab21-4079-9cb1-596358ee5dd1";

            //HostDatastoreSystemMo HostDatastoreSystemMo = HostMo.getHostDatastoreSystemMo();
            HostScsiDiskPartition extent = vmfsDatastoreInfo.getVmfs().getExtent().get(0);
            String devicePath = "/vmfs/devices/disks/" + extent.getDiskName();
            //boolean b = devicePath.equalsIgnoreCase(deviceName);
            List<HostScsiDisk> hostScsiDisks = HostDatastoreSystemMo.queryAvailableDisksForVmfs();
//            boolean equals = false;
//            for (HostScsiDisk hostScsiDisk : hostScsiDisks) {
//                if (hostScsiDisk.getDeviceName().equals(deviceName)) {
//                    equals = true;
//                }
//            }
//            System.out.println(equals);

//            List<HostScsiDisk> hostScsiDisks = HostDatastoreSystemMo.queryAvailableSsds();
//            boolean equals = false;
//            for (HostScsiDisk hostScsiDisk : hostScsiDisks) {
//                if (hostScsiDisk.getDeviceName().equals(deviceName)) {
//                    equals = true;
//                }
//            }
//
//            System.out.println(equals);
//            List<HostUnresolvedVmfsVolume> hostUnresolvedVmfsVolumes = HostDatastoreSystemMo.queryUnresolvedVmfsVolume();
//            List<HostHostBusAdapter> hbas = HostMo.getHostStorageSystemMo().getStorageDeviceInfo().getHostBusAdapter();



//            if (vmfsDatastoreOptions != null && vmfsDatastoreOptions.size() > 0) {
//                VmfsDatastoreOption vmfsDatastoreOption = vmfsDatastoreOptions.get(0);
//                VmfsDatastoreExpandSpec spec = (VmfsDatastoreExpandSpec) vmfsDatastoreOption.getSpec();
//
//                long addTotalSectors = 1 * 1l * 1024 * 1024 * 1024 / 512 ;  //2097152
//
//                spec.getPartition().getPartition().get(0).setEndSector(currentEndSector+addTotalSectors);
//                //spec.getPartition().setTotalSectors(originSectors+totalSectors);
//                HostDatastoreSystemMo.expandVmfsDatastore(dsMo, spec);
//            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
