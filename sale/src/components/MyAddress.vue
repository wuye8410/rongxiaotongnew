<template>
  <!-- 主内容区 -->
  <div class="max-w-2xl mx-auto pr-48">
    <!-- 收货地址 -->
    <div class="space-y-6">
      <!-- 已有地址列表 -->
      <div
        v-for="(addr, index) in shippingAddresses"
        :key="index"
        class="border rounded-lg p-4 relative"
      >
        <div class="absolute top-2 right-2 flex space-x-2">
          <button
            @click="editAddress(index)"
            class="text-blue-500 hover:text-blue-700"
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
          <span
            v-if="addr.isDefault"
            class="ml-auto px-2 py-0.5 mt-3 text-xs bg-green-100 text-green-800 rounded"
            >默认</span
          >
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

      <!-- 添加/编辑地址表单弹窗 -->
      <el-dialog
        v-model="showAddressForm"
        :title="editingAddressIndex === -1 ? '添加新地址' : '编辑地址'"
        width="600px"
      >
        <div class="space-y-4 px-4">
          <div class="grid grid-cols-2 gap-4">
          <div class="space-y-1">
            <label class="text-sm text-gray-600">收货人</label>
            <input
              type="text"
              v-model="currentAddress.consignee"
              class="w-full border border-gray-300 rounded-md px-3 py-2 focus:outline-none focus:ring-1 focus:ring-green-500"
              placeholder="请输入收货人姓名"
            />
          </div>

          <div class="space-y-1">
            <label class="text-sm text-gray-600">手机号码</label>
            <input
              type="text"
              v-model="currentAddress.phone"
              class="w-full border border-gray-300 rounded-md px-3 py-2 focus:outline-none focus:ring-1 focus:ring-green-500"
              placeholder="请输入手机号码"
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
            >设为默认收货地址</label
          >
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
  </div>
</template>

<script setup>
import { ref, reactive } from "vue";
import { onMounted } from "vue";
import { UserIcon } from "lucide-vue-next";
import { defineComponent } from "vue";
import { apiClient } from "../api/apiService.js";
import { useRouter } from "vue-router";
import { ElMessage, ElDialog, ElButton } from "element-plus";
import { useStore } from "vuex";
// 菜单展开状态
const expandedMenus = reactive({
  myInfo: true,
  myPosts: false,
});

// 切换菜单展开状态
const toggleMenu = (menu) => {
  expandedMenus[menu] = !expandedMenus[menu];
};



// 收货地址数据
const shippingAddresses = ref([]);

onMounted(async () => {
  //检索地址信息
  const addData = await selectPersAdd();
  shippingAddresses.value  = addData;
  
});

// 查询个人信息接口
const selectPersAdd = async () => {
  try {
    const response = await apiClient.get("/address/selectByOwnName", {
      headers: {
        Authorization: window.localStorage.token,
      },
    });
    console.log("请求成功", response.data);
    if (response.flag == true) {
      return response.data;
    } else {
      console.error("请求失败", error);
    }
  } catch (error) {
    console.error("请求失败", error);
    throw error;
  }
};

// 地址表单控制
const showAddressForm = ref(false);
const editingAddressIndex = ref(-1);
const currentAddress = reactive({
  id:"",
  ownName:"",
  consignee: "",
  phone: "",
  detailAddress: "",
  isDefault: false,
});

// 编辑地址
const editAddress = (index) => {
  const addr = shippingAddresses.value[index];
  currentAddress.id = addr.id;
  currentAddress.consignee = addr.consignee;
  currentAddress.ownName = addr.ownName;
  currentAddress.phone = addr.phone;
  currentAddress.detailAddress = addr.addressDetail;
  currentAddress.isDefault = addr.isDefault;

  editingAddressIndex.value = index;
  showAddressForm.value = true;
};

// 删除地址
const deleteAddress = async(addr) => {
  try{
    if (confirm("确定要删除这个地址吗？")) {
      if(addr.isDefault){
        ElMessage.error("该地址为默认地址不能删除。");
        return;
      }
      //删除
      const response = await apiClient.delete(`/address/delete/${addr.id}`, {
        headers: {
          Authorization: window.localStorage.token,
          "Content-Type": "application/json"
        }
      });
      console.log('Delete response:', response); // Debug logging
      if (response.flag == true) {
        ElMessage.success("删除地址成功。");
      }
      else {
        ElMessage.error("删除地址失败，请重试。");
      }

      //重新获取地址
      const addData = await selectPersAdd();
      shippingAddresses.value  = addData;
    }
  }  catch (error) {
    console.error('删除地址失败，请重试:', error);
  }
};

// 保存地址
const saveAddress = async() => {
  try{
    const param = {
      id: currentAddress.id,
      consignee: currentAddress.consignee,
      phone: currentAddress.phone,
      ownName:currentAddress.ownName,
      addressDetail: currentAddress.detailAddress,
      isDefault: currentAddress.isDefault,
    };


    if (editingAddressIndex.value === -1) {
      // 添加新地址
      const response = await apiClient.post(`/address/add`, param, {
      headers: {
        "Content-Type": "application/json",
        Authorization: window.localStorage.token,
      },
      });

      if (response.flag) {
        ElMessage.success("添加地址成功。");
      } else {
        ElMessage.error(response.data);
      }
    } else {
      // 更新现有地址
      const response = await apiClient.post(`/address/defaultAddressInfoUpdate`, param, {
      headers: {
        "Content-Type": "application/json",
        Authorization: window.localStorage.token,
      },
      });

      if (response.flag) {
        ElMessage.success("更新地址成功。");
      } else {
        ElMessage.error(response.data);
      }
    }

    //重新获取地址
    const addData = await selectPersAdd();
    shippingAddresses.value  = addData;

    // 重置表单
    resetAddressForm();

  }  catch (error) {
    console.error('咨询失败，请重试:', error);
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

  editingAddressIndex.value = -1;
  showAddressForm.value = false;
};

</script>

<style scoped>
/* 可以添加额外的样式 */
</style>
