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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class BaseMo {
    private static final Logger s_logger = LoggerFactory.getLogger(BaseMo.class);

    protected VmwareContext _context;
    protected ManagedObjectReference _mor;

    private String _name;

    public BaseMo(VmwareContext context, ManagedObjectReference mor) {
        assert (context != null);

        _context = context;
        _mor = mor;
    }

    public BaseMo(VmwareContext context, String morType, String morValue) {
        assert (context != null);
        assert (morType != null);
        assert (morValue != null);

        _context = context;
        _mor = new ManagedObjectReference();
        _mor.setType(morType);
        _mor.setValue(morValue);
    }

    public VmwareContext getContext() {
        return _context;
    }

    public ManagedObjectReference getMor() {
        assert (_mor != null);
        return _mor;
    }

    public ManagedObjectReference getParentMor() throws Exception {
        return (ManagedObjectReference)_context.getVimClient().getDynamicProperty(_mor, "parent");
    }

    public String getName() throws Exception {
        if (_name == null)
            _name = (String)_context.getVimClient().getDynamicProperty(_mor, "name");

        return _name;
    }

    public void unregisterVm() throws Exception {
        _context.getService().unregisterVM(_mor);
    }

    public boolean destroy() throws Exception {
        ManagedObjectReference morTask = _context.getService().destroyTask(_mor);

        boolean result = _context.getVimClient().waitForTask(morTask);
        if (result) {
            _context.waitForTaskProgressDone(morTask);
            return true;
        } else {
            s_logger.error("VMware destroy_Task failed due to " + TaskMo.getTaskFailureInfo(_context, morTask));
        }
        return false;
    }

    public void reload() throws Exception {
        _context.getService().reload(_mor);
    }

    public boolean rename(String newName) throws Exception {
        ManagedObjectReference morTask = _context.getService().renameTask(_mor, newName);

        boolean result = _context.getVimClient().waitForTask(morTask);
        if (result) {
            _context.waitForTaskProgressDone(morTask);
            return true;
        } else {
            s_logger.error("VMware rename_Task failed due to " + TaskMo.getTaskFailureInfo(_context, morTask));
        }
        return false;
    }

    public void setCustomFieldValue(String fieldName, String value) throws Exception {
        CustomFieldsManagerMo cfmMo = new CustomFieldsManagerMo(_context, _context.getServiceContent().getCustomFieldsManager());

        int key = getCustomFieldKey(fieldName);
        if (key == 0) {
            try {
                CustomFieldDef field = cfmMo.addCustomerFieldDef(fieldName, getMor().getType(), null, null);
                key = field.getKey();
            } catch (Exception e) {
                // assuming the exception is caused by concurrent operation from other places
                // so we retieve the key again
                key = getCustomFieldKey(fieldName);
            }
        }

        if (key == 0)
            throw new Exception("Unable to setup custom field facility");

        cfmMo.setField(getMor(), key, value);
    }

    public String getCustomFieldValue(String fieldName) throws Exception {
        int key = getCustomFieldKey(fieldName);
        if (key == 0)
            return null;

        CustomFieldStringValue cfValue = (CustomFieldStringValue)_context.getVimClient().getDynamicProperty(getMor(), String.format("value[%d]", key));
        if (cfValue != null)
            return cfValue.getValue();

        return null;
    }

    public String getAllcustom() throws Exception {

        _context.getVimClient().getDynamicProperty(getMor(), "summary.customValue");


        return null;
    }

    public int getCustomFieldKey(String fieldName) throws Exception {
        return getCustomFieldKey(getMor().getType(), fieldName);
    }

    public int getCustomFieldKey(String morType, String fieldName) throws Exception {
        assert (morType != null);

        CustomFieldsManagerMo cfmMo = new CustomFieldsManagerMo(_context, _context.getServiceContent().getCustomFieldsManager());

        return cfmMo.getCustomFieldKey(morType, fieldName);
    }

    protected ObjectContent retrieveObjectProperties(List<String> properties) {
        ObjectSpec oSpec = creatObjectSpec(this._mor, Boolean.FALSE, null);
        PropertySpec pSpec = createPropertySpec(this._mor.getType(), properties == null || properties.size() == 0, properties);
        PropertyFilterSpec pfSpec = new PropertyFilterSpec();
        pfSpec.getObjectSet().add(oSpec);
        pfSpec.getPropSet().add(pSpec);
        List<PropertyFilterSpec> pfSpecArr = new ArrayList<PropertyFilterSpec>();
        pfSpecArr.add(pfSpec);
        List<ObjectContent> objs;
        try {
            objs = this.getContext().getService().retrieveProperties(this._mor,pfSpecArr);
        } catch (Exception var8) {
            throw new RuntimeException(var8);
        }

        return objs != null && objs.get(0) != null ? objs.get(0) : null;
    }

    protected Object getCurrentProperty(String propertyName) {
        List<String > properts=new ArrayList<>();
        properts.add(propertyName);
        ObjectContent objContent = this.retrieveObjectProperties(properts);
        Object propertyValue = null;
        if (objContent != null) {
            List<DynamicProperty> dynaProps = objContent.getPropSet();
            if (dynaProps != null && dynaProps.get(0) != null) {
                propertyValue = convertProperty(dynaProps.get(0).getVal());
            }
        }

        return propertyValue;
    }

    public static ObjectSpec creatObjectSpec(ManagedObjectReference mor, boolean skip, Collection<SelectionSpec> selSet) {
        ObjectSpec oSpec = new ObjectSpec();
        oSpec.setObj(mor);
        oSpec.setSkip(skip);
        oSpec.getSelectSet().addAll(selSet);
        return oSpec;
    }

    public static PropertySpec createPropertySpec(String type, boolean allProp, Collection<String> pathSet) {
        PropertySpec pSpec = new PropertySpec();
        pSpec.setType(type);
        pSpec.setAll(allProp);
        pSpec.getPathSet().addAll(pathSet);
        return pSpec;
    }

    public static Object convertProperty(Object dynaPropVal) {
        Object propertyValue = null;
        if (dynaPropVal == null) {
            throw new IllegalArgumentException("Unable to convertProperty on null object.");
        } else {
            Class<?> propClass = dynaPropVal.getClass();
            String propName = propClass.getName();
            if (propName.contains("ArrayOf")) {
                String methodName = propName.substring(propName.indexOf("ArrayOf") + "ArrayOf".length());

                try {
                    Method getMethod;
                    try {
                        getMethod = propClass.getMethod("get" + methodName, (Class[])null);
                    } catch (NoSuchMethodException var7) {
                        getMethod = propClass.getMethod("get_" + methodName.toLowerCase(), (Class[])null);
                    }

                    propertyValue = getMethod.invoke(dynaPropVal, (Object[])null);
                } catch (Exception var8) {
                    s_logger.error("Exception caught trying to convertProperty", var8);
                }
            } else if (dynaPropVal.getClass().isArray()) {
                propertyValue = dynaPropVal;
            } else {
                propertyValue = dynaPropVal;
            }

            return propertyValue;
        }
    }
}
