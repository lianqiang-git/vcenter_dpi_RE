package com.ghca.vm.mo;

import com.ghca.vm.util.Pair;
import com.ghca.vm.util.VmwareContext;
import com.vmware.vim25.InvalidPropertyFaultMsg;
import com.vmware.vim25.ManagedObjectReference;
import com.vmware.vim25.RuntimeFaultFaultMsg;

import java.util.List;

public class RootFsMo extends BaseMo {
    public RootFsMo(VmwareContext context, ManagedObjectReference mor) {
        super(context, mor);
    }

    public RootFsMo(VmwareContext context, String morType, String morValue) {
        super(context, morType, morValue);
    }

    public List<Pair<ManagedObjectReference, String>> getAllDatacenterOnRootFs() throws Exception {
        //查找datacenters
        List<Pair<ManagedObjectReference, String>> datacenters = _context.inFolderByType(_context.getRootFolder(),
                "Datacenter");
        return datacenters;
    }

    /**
     * 获取host主机列表
     */
    public List<Pair<ManagedObjectReference, String>> getAllHostOnRootFs()
            throws InvalidPropertyFaultMsg, RuntimeFaultFaultMsg {
        //查找hosts
        List<Pair<ManagedObjectReference, String>> hosts = _context.inFolderByType(_context.getRootFolder(),
                "HostSystem");
        return hosts;
    }

    /**
     * 获取集群列表
     */
    public List<Pair<ManagedObjectReference, String>> getAllClusterOnRootFs()
            throws InvalidPropertyFaultMsg, RuntimeFaultFaultMsg {
        //查找clusters
        List<Pair<ManagedObjectReference, String>> clusters = _context.inFolderByType(_context.getRootFolder(),
                "ClusterComputeResource");
        return clusters;
    }

    /**
     * 获取datastore列表
     */
    public List<Pair<ManagedObjectReference, String>> getAllDatastoreOnRootFs()
            throws InvalidPropertyFaultMsg, RuntimeFaultFaultMsg {
        //查找datastores
        List<Pair<ManagedObjectReference, String>> hosts = _context.inFolderByType(_context.getRootFolder(),
                "Datastore");
        return hosts;
    }

    public HostMo findHost(String name) throws Exception {
        HostMo objmo = null;
        List<Pair<ManagedObjectReference, String>> objs = getAllHostOnRootFs();
        if (objs != null && objs.size() > 0) {
            for (Pair<ManagedObjectReference, String> obj : objs) {
                HostMo submo = new HostMo(_context, obj.first());
                if(name.equals(submo.getName())){
                    objmo = submo;
                    break;
                }
            }
        }
        return objmo;
    }

    public HostMo findHostById(String id) throws Exception {
        HostMo objmo = null;
        List<Pair<ManagedObjectReference, String>> objs = getAllHostOnRootFs();
        if (objs != null && objs.size() > 0) {
            for (Pair<ManagedObjectReference, String> obj : objs) {
                HostMo submo = new HostMo(_context, obj.first());
                if(id.equals(submo.getMor().getValue())){
                    objmo = submo;
                    break;
                }
            }
        }
        return objmo;
    }

    public ClusterMo findCluster(String name) throws Exception {
        ClusterMo objmo = null;
        List<Pair<ManagedObjectReference, String>> objs = getAllClusterOnRootFs();
        if (objs != null && objs.size() > 0) {
            for (Pair<ManagedObjectReference, String> obj : objs) {
                ClusterMo submo = new ClusterMo(_context, obj.first());
                if(name.equals(submo.getName())){
                    objmo = submo;
                    break;
                }
            }
        }
        return objmo;
    }

    public ClusterMo findClusterById(String id) throws Exception {
        ClusterMo objmo = null;
        List<Pair<ManagedObjectReference, String>> objs = getAllClusterOnRootFs();
        if (objs != null && objs.size() > 0) {
            for (Pair<ManagedObjectReference, String> obj : objs) {
                ClusterMo submo = new ClusterMo(_context, obj.first());
                if(id.equals(submo.getMor().getValue())){
                    objmo = submo;
                    break;
                }
            }
        }
        return objmo;
    }

    public DatastoreMo findDataStore(String name) throws Exception {
        DatastoreMo objmo = null;
        List<Pair<ManagedObjectReference, String>> objs = getAllDatastoreOnRootFs();
        if (objs != null && objs.size() > 0) {
            for (Pair<ManagedObjectReference, String> obj : objs) {
                DatastoreMo submo = new DatastoreMo(_context, obj.first());
                if(name.equals(submo.getName())){
                    objmo = submo;
                    break;
                }
            }
        }
        return objmo;
    }

    public DatastoreMo findDataStoreById(String id) throws Exception {
        DatastoreMo objmo = null;
        List<Pair<ManagedObjectReference, String>> objs = getAllDatastoreOnRootFs();
        if (objs != null && objs.size() > 0) {
            for (Pair<ManagedObjectReference, String> obj : objs) {
                DatastoreMo submo = new DatastoreMo(_context, obj.first());
                if(id.equals(submo.getMor().getValue())){
                    objmo = submo;
                    break;
                }
            }
        }
        return objmo;
    }
}
