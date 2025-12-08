<template>
  <!-- 主内容区 -->
  <div class="max-w-2xl mx-auto pr-48">
    <!-- 收货地址 -->
    <div class="space-y-6">
      <!-- 已有地址列表 -->
      <div
          v-for="(addr, index) in shippingAddresses"
          :key="addr.id"
          class="border rounded-lg p-4 relative"
      >
        <div class="absolute top-2 right-2 flex space-x-2">
          <button
              @click="editAddress(index)"
              class="text-blue-500 hover:text-blue-700"
              title="编辑"
          >
            <svg
                xmlns="http://www.w3.org/2000/svg"
                width="16"
                height="16"
                viewBox="0 0 24 24"
                fill="none"
                stroke="currentColor"
                stroke-width="2"
                stroke-linecap="round"
                stroke-linejoin="round"
                class="lucide lucide-pencil"
            >
              <path d="M17 3a2.85 2.83 0 1 1 4 4L7.5 20.5 2 22l1.5-5.5Z" />
              <path d="m15 5 4 4" />
            </svg>
          </button>
          <button
              @click="deleteAddress(addr)"
              class="text-red-500 hover:text-red-700"
              title="删除"
          >
            <svg
                xmlns="http://www.w3.org/2000/svg"
                width="16"
                height="16"
                viewBox="0 0 24 24"
                fill="none"
                stroke="currentColor"
                stroke-width="2"
                stroke-linecap="round"
                stroke-linejoin="round"
                class="lucide lucide-trash-2"
            >
              <path d="M3 6h18" />
              <path d="M19 6v14c0 1-1 2-2 2H7c-1 0-2-1-2-2V6" />
              <path d="M8 6V4c0-1 1-2 2-2h4c1 0 2 1 2 2v2" />
              <path d="M10 11v6" />
              <path d="M14 11v6" />
            </svg>
          </button>
        </div>

        <div class="flex items-center mb-2">
          <span class="font-medium">{{ addr.consignee }}</span>
          <span class="ml-4 text-gray-600">{{ addr.phone }}</span>
          <!-- 显示标签 -->
          <span v-if="addr.tag" class="ml-2 px-2 py-0.5 text-xs bg-blue-100 text-blue-800 rounded">
            {{ addr.tag }}
          </span>
          <!-- 修复：移除行内注释 -->
          <span
              v-if="addr.is_default === 1 || addr.isDefault === 1"
              class="ml-auto px-2 py-0.5 mt-3 text-xs bg-green-100 text-green-800 rounded"
          >
            默认
          </span>
        </div>

        <!-- 修复：移除行内注释 -->
        <div class="text-gray-600">{{ addr.address_detail || addr.addressDetail }}</div>
      </div>

      <!-- 添加新地址按钮 -->
      <div
          class="border border-dashed rounded-lg p-4 flex items-center justify-center cursor-pointer hover:bg-gray-50"
          @click="showAddressForm = true"
      >
        <svg
            xmlns="http://www.w3.org/2000/svg"
            width="20"
            height="20"
            viewBox="0 0 24 24"
            fill="none"
            stroke="currentColor"
            stroke-width="2"
            stroke-linecap="round"
            stroke-linejoin="round"
            class="lucide lucide-plus"
        >
          <path d="M5 12h14" />
          <path d="M12 5v14" />
        </svg>
        <span class="ml-2 text-gray-600">添加新地址</span>
      </div>
    </div>

    <!-- 添加/编辑地址表单弹窗 -->
    <el-dialog
        v-model="showAddressForm"
        :title="editingAddressIndex === -1 ? '添加新地址' : '编辑地址'"
        width="600px"
    >
      <div class="space-y-4 px-4">
        <div class="grid grid-cols-2 gap-4">
          <div class="space-y-1">
            <!-- 地址标签输入框 -->
            <div class="space-y-1">
              <label class="text-sm text-gray-600">地址标签（如：家庭、公司）</label>
              <input
                  type="text"
                  v-model="currentAddress.tag"
                  class="w-full border border-gray-300 rounded-md px-3 py-2 focus:outline-none focus:ring-1 focus:ring-green-500"
                  placeholder="请输入地址标签，方便区分"
              />
            </div>
            <label class="text-sm text-gray-600">收货人</label>
            <input
                type="text"
                v-model="currentAddress.consignee"
                class="w-full border border-gray-300 rounded-md px-3 py-2 focus:outline-none focus:ring-1 focus:ring-green-500"
                placeholder="请输入收货人姓名"
                required
            />
          </div>

          <div class="space-y-1">
            <label class="text-sm text-gray-600">手机号码</label>
            <input
                type="text"
                v-model="currentAddress.phone"
                class="w-full border border-gray-300 rounded-md px-3 py-2 focus:outline-none focus:ring-1 focus:ring-green-500"
                placeholder="请输入手机号码"
                required
                pattern="^1[3-9]\d{9}$"
            />
          </div>
        </div>

        <div class="space-y-1">
          <label class="text-sm text-gray-600">详细地址</label>
          <input
              type="text"
              v-model="currentAddress.detailAddress"
              class="w-full border border-gray-300 rounded-md px-3 py-2 focus:outline-none focus:ring-1 focus:ring-green-500"
              placeholder="请输入详细地址，如街道、门牌号等"
              required
          />
        </div>

        <div class="flex items-center">
          <input
              type="checkbox"
              id="defaultAddress"
              v-model="currentAddress.isDefault"
              class="rounded text-green-600 focus:ring-green-500"
          />
          <label for="defaultAddress" class="ml-2 text-sm text-gray-600"
          >设为默认收货地址</label>
        </div>
      </div>
      <template #footer>
        <span class="dialog-footer">
          <el-button @click="resetAddressForm();showAddressForm = false">取消</el-button>
          <el-button type="primary" @click="saveAddress">保存</el-button>
        </span>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, reactive, onMounted } from "vue";
import { apiClient } from "../api/apiService.js";
import { ElMessage } from "element-plus";

// 收货地址数据
const shippingAddresses = ref([]);

// 页面加载时查询地址
onMounted(async () => {
  await fetchAddresses();
});

// 查询地址列表 - 修复版
const fetchAddresses = async () => {
  try {
    console.log("开始获取地址列表...");

    // 直接使用 fetch API 进行调试
    const response = await fetch('http://localhost:9090/address/selectByOwnName', {
      method: 'GET',
      headers: {
        'Authorization': window.localStorage.token,
        'Content-Type': 'application/json'
      }
    });

    console.log("响应状态：", response.status);
    console.log("响应头：", response.headers);

    // 获取原始响应文本
    const responseText = await response.text();
    console.log("原始响应文本：", responseText);

    // 尝试解析JSON
    let data;
    try {
      data = JSON.parse(responseText);
      console.log("解析后的JSON数据：", data);
    } catch (e) {
      console.error("JSON解析失败：", e);
      console.log("响应文本可能不是JSON格式");
      // 尝试解码URL编码的文本
      try {
        const decodedText = decodeURIComponent(responseText);
        console.log("解码后的文本：", decodedText);
        // 再次尝试解析
        try {
          data = JSON.parse(decodedText);
        } catch (e2) {
          data = { flag: false, message: "响应格式错误" };
        }
      } catch (decodeError) {
        data = { flag: false, message: "响应解码失败" };
      }
    }

    // 处理响应数据
    if (data && data.flag === true) {
      // 数据字段处理
      const addresses = Array.isArray(data.data) ? data.data.map(addr => ({
        id: addr.id,
        ownName: addr.own_name || addr.ownName,
        consignee: addr.consignee || '',
        phone: addr.phone || '',
        addressDetail: addr.address_detail || addr.addressDetail || '',
        isDefault: addr.is_default === 1 || addr.isDefault === 1 || addr.isDefault === true,
        tag: addr.tag || ''
      })) : [];

      shippingAddresses.value = addresses;
      console.log("处理后的地址数据：", shippingAddresses.value);
      console.log("地址数量：", shippingAddresses.value.length);

      if (shippingAddresses.value.length === 0) {
        ElMessage.info("暂无收货地址，请添加新地址");
      }
    } else {
      console.error("接口返回错误：", data);
      const errorMsg = data?.message || "获取地址失败";
      ElMessage.error(errorMsg);
    }
  } catch (error) {
    console.error("请求失败", error);
    ElMessage.error("网络错误，获取地址失败：" + error.message);
  }
};

// 地址表单控制
const showAddressForm = ref(false);
const editingAddressIndex = ref(-1);
const currentAddress = reactive({
  id: "",
  ownName: "",
  consignee: "",
  phone: "",
  detailAddress: "",
  isDefault: false,
  tag: ""
});

// 编辑地址
const editAddress = (index) => {
  const addr = shippingAddresses.value[index];
  console.log("编辑地址原始数据：", addr);

  currentAddress.id = addr.id;
  currentAddress.consignee = addr.consignee;
  currentAddress.ownName = addr.ownName || window.localStorage.userName;
  currentAddress.phone = addr.phone;
  currentAddress.detailAddress = addr.addressDetail;
  currentAddress.isDefault = addr.isDefault === true || addr.isDefault === 1;
  currentAddress.tag = addr.tag || "";
  editingAddressIndex.value = index;
  showAddressForm.value = true;
};

// 删除地址
const deleteAddress = async (addr) => {
  try {
    if (addr.isDefault) {
      ElMessage.error("该地址为默认地址不能删除");
      return;
    }

    if (confirm("确定要删除这个地址吗？")) {
      // 使用 fetch API
      const response = await fetch(`http://localhost:9090/address/delete/${addr.id}`, {
        method: 'DELETE',
        headers: {
          'Authorization': window.localStorage.token,
          'Content-Type': 'application/json'
        }
      });

      const result = await response.json();
      console.log("删除响应：", result);

      if (result.flag) {
        ElMessage.success("删除地址成功");
        await fetchAddresses();  // 重新获取地址列表
      } else {
        ElMessage.error("删除失败：" + (result.message || "未知错误"));
      }
    }
  } catch (error) {
    console.error("删除失败", error);
    ElMessage.error("网络错误，删除失败");
  }
};

// 保存地址
const saveAddress = async () => {
  // 前端校验
  if (!currentAddress.consignee) {
    ElMessage.warning("请输入收货人姓名");
    return;
  }
  if (!currentAddress.phone || !/^1[3-9]\d{9}$/.test(currentAddress.phone)) {
    ElMessage.warning("请输入正确的手机号码");
    return;
  }
  if (!currentAddress.detailAddress) {
    ElMessage.warning("请输入详细地址");
    return;
  }
  // 新增：更严格的地址格式校验
  const address = currentAddress.detailAddress.trim();

  // 规则1：必须包含中文（最基本要求）
  const hasChinese = /[\u4e00-\u9fa5]/.test(address);
  if (!hasChinese) {
    ElMessage.warning("详细地址必须包含中文");
    return;
  }

  // 规则2：地址长度至少6个字符
  if (address.length < 6) {
    ElMessage.warning("详细地址太短，请填写完整地址");
    return;
  }

  // 规则3：禁止连续特殊字符（如##@!!）
  const hasInvalidSpecialChars = /[#@!]{3,}/.test(address);
  if (hasInvalidSpecialChars) {
    ElMessage.warning("地址包含无效的特殊字符");
    return;
  }

  // 规则4：地址结构应该合理（至少包含路、街、巷等关键词）
  const validKeywords = ['路', '街', '巷', '道', '号', '小区', '村', '组', '栋', '单元', '室', '楼'];
  const hasValidStructure = validKeywords.some(keyword => address.includes(keyword));
  if (!hasValidStructure) {
    ElMessage.warning("请填写详细到门牌号的地址（应包含路、街、号、小区等）");
    return;
  }

  // 规则5：禁止纯数字或数字+字母的组合
  const isOnlyAlphanumeric = /^[a-zA-Z0-9]+$/.test(address);
  if (isOnlyAlphanumeric) {
    ElMessage.warning("地址不能仅为字母和数字的组合");
    return;
  }
  try {
    const param = {
      id: currentAddress.id,
      consignee: currentAddress.consignee,
      phone: currentAddress.phone,
      ownName: currentAddress.ownName || window.localStorage.userName,
      addressDetail: currentAddress.detailAddress,
      isDefault: currentAddress.isDefault ? 1 : 0,
      tag: currentAddress.tag
    };

    console.log("保存参数：", param);

    const url = editingAddressIndex.value === -1
        ? 'http://localhost:9090/address/add'
        : 'http://localhost:9090/address/defaultAddressInfoUpdate';

    const method = 'POST';

    const response = await fetch(url, {
      method: method,
      headers: {
        'Content-Type': 'application/json',
        'Authorization': window.localStorage.token,
      },
      body: JSON.stringify(param)
    });

    const result = await response.json();
    console.log("保存响应：", result);

    if (result.flag) {
      ElMessage.success(editingAddressIndex.value === -1 ? "添加地址成功" : "更新地址成功");
      await fetchAddresses();  // 重新获取地址列表
      resetAddressForm();
      showAddressForm.value = false;
    } else {
      const errorMsg = result.message || (editingAddressIndex.value === -1 ? "添加失败" : "更新失败");
      ElMessage.error(errorMsg);
    }
  } catch (error) {
    console.error("保存失败", error);
    ElMessage.error("网络错误，保存失败");
  }
};

// 重置地址表单
const resetAddressForm = () => {
  currentAddress.id = "";
  currentAddress.consignee = "";
  currentAddress.ownName = "";
  currentAddress.phone = "";
  currentAddress.detailAddress = "";
  currentAddress.isDefault = false;
  currentAddress.tag = "";
  editingAddressIndex.value = -1;
};
</script>

<style scoped>
.dialog-footer button:first-child {
  margin-right: 10px;
}
</style>