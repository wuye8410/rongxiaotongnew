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
            {{ addr.tag}}
          </span>
          <span
              v-if="addr.isDefault"
              class="ml-auto px-2 py-0.5 mt-3 text-xs bg-green-100 text-green-800 rounded"
          >默认</span>
        </div>

        <div class="text-gray-600">{{ addr.addressDetail }}</div>
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
    </div> <!-- 这是第173行div的闭合标签 -->

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
  </div> <!-- 这是第3行div的闭合标签 -->
</template>
<script setup>
import { ref, reactive } from "vue";
import { onMounted } from "vue";
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
    console.log("开始获取地址列表..."); // 调试
    const response = await apiClient.get("/address/selectByOwnName", {
      headers: {
        Authorization: window.localStorage.token,
      },
    });

    console.log("地址接口响应：", response); // 调试

    // 根据日志，后端返回格式是 {flag: boolean, data: ...}
    if (response.flag) {
      shippingAddresses.value = response.data || [];
      console.log("成功获取地址数据，数量：", shippingAddresses.value.length);
      console.log("地址数据详情：", shippingAddresses.value);
    } else {
      ElMessage.error("获取地址失败：" + (response.message || response.data || "未知错误"));
    }
  } catch (error) {
    console.error("请求失败", error);
    ElMessage.error("网络错误，获取地址失败");
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
  currentAddress.id = addr.id;
  currentAddress.consignee = addr.consignee;
  currentAddress.ownName = addr.ownName;
  currentAddress.phone = addr.phone;
  currentAddress.detailAddress = addr.addressDetail; // 注意字段名映射
  currentAddress.isDefault = addr.isDefault === 1; // 转换为布尔值
  currentAddress.tag = addr.tag;
  editingAddressIndex.value = index;
  showAddressForm.value = true;
};

// 删除地址 - 修复版
const deleteAddress = async (addr) => {
  try {
    if (addr.isDefault) {
      ElMessage.error("该地址为默认地址不能删除");
      return;
    }
    if (confirm("确定要删除这个地址吗？")) {
      const response = await apiClient.delete(`/address/delete/${addr.id}`, {
        headers: {
          Authorization: window.localStorage.token,
        },
      });
      console.log("删除响应：", response); // 调试

      if (response.flag) { // 改为 flag
        ElMessage.success("删除地址成功");
        await fetchAddresses();  // 重新获取地址列表
      } else {
        ElMessage.error("删除失败：" + (response.message || response.data || "未知错误"));
      }
    }
  } catch (error) {
    console.error("删除失败", error);
    ElMessage.error("网络错误，删除失败");
  }
};

// 保存地址 - 修复版
const saveAddress = async () => {
  // 前端简单校验
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

  try {
    const param = {
      id: currentAddress.id,
      consignee: currentAddress.consignee,
      phone: currentAddress.phone,
      ownName: currentAddress.ownName || window.localStorage.userName, // 确保有用户名
      addressDetail: currentAddress.detailAddress,
      isDefault: currentAddress.isDefault ? 1 : 0, // 转换为数字
      tag: currentAddress.tag
    };

    console.log("保存参数：", param); // 调试

    let response;
    if (editingAddressIndex.value === -1) {
      // 添加新地址
      response = await apiClient.post("/address/add", param, {
        headers: {
          "Content-Type": "application/json",
          Authorization: window.localStorage.token,
        },
      });
    } else {
      // 更新地址
      response = await apiClient.post("/address/defaultAddressInfoUpdate", param, {
        headers: {
          "Content-Type": "application/json",
          Authorization: window.localStorage.token,
        },
      });
    }

    console.log("保存响应：", response); // 调试

    if (response.flag) { // 改为 flag
      ElMessage.success(editingAddressIndex.value === -1 ? "添加地址成功" : "更新地址成功");
      await fetchAddresses();  // 重新获取地址列表
      resetAddressForm();
      showAddressForm.value = false;
    } else {
      ElMessage.error(response.message || response.data || (editingAddressIndex.value === -1 ? "添加失败" : "更新失败"));
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
/* 可以添加额外的样式 */
</style>