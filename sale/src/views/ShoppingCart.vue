<template>
 <section class="py-12 bg-white border-t-2 border-gray-300">
    <div class="container w-2/3 mx-auto">
      <!-- Back Button -->
      <div class="mb-6">
        <button class="flex items-center text-gray-500 hover:text-gray-700" @click="goBack">
          <svg
            xmlns="http://www.w3.org/2000/svg"
            class="h-4 w-4 mr-1"
            fill="none"
            viewBox="0 0 24 24"
            stroke="currentColor"
          >
            <path
              stroke-linecap="round"
              stroke-linejoin="round"
              stroke-width="2"
              d="M15 19l-7-7 7-7"
            />
          </svg>
          返回
        </button>
      </div>

      <!-- 收货地址栏 -->
      <div class="bg-white rounded-lg shadow-md p-6 mb-6">
        <div class="flex justify-between items-start">
          <div>
            <h2 class="text-lg font-semibold mb-2">收货地址</h2>
            <div class="space-y-1 text-gray-700">
              <p><span class="font-medium">{{ addressName }}</span> <span class="ml-4">{{ addressPhone }}</span></p>
              <p>{{ addressAddress }}</p>
            </div>
          </div>
          <button 
            @click="showAllAddress();showAddressModal = true" 
            class="px-4 py-2 mt-6 bg-white border border-gray-300 rounded-md text-sm font-medium text-gray-700 hover:bg-gray-50"
          >
            更换地址
          </button>
        </div>
      </div>
      
     <!-- 顶部操作栏 -->
    <div class="flex items-center mb-4 text-sm">
      <div class="flex items-center">
        <input type="checkbox" class="w-4 h-4 mr-2" v-model="selectAll" @change="toggleSelectAll" />
        <span class="mr-4">全选</span>
      </div>
      <button class="px-3 py-1 border border-gray-300 rounded text-gray-600 mr-4" @click="deleteSelected">删除</button>
      <span class="text-gray-500">总计商品: {{ totalItems }}</span>
    </div>

    <!-- 购物车列表 -->
    <div class="space-y-4">
      <div v-for="(item, index) in cartItems" :key="index" class="border border-gray-100 p-4 rounded-md">
        <div class="flex items-start">
          <!-- 选择框 -->
          <div class="mr-3 pt-1">
            <input type="checkbox" class="w-4 h-4" v-model="item.selected" />
          </div>
          
          <!-- 商品图片 -->
          <div class="w-20 h-20 mr-4 flex-shrink-0">
            <img :src="$store.state.imgShowRoad + '/file/order/' + item.picture" class="w-full h-full object-cover" />

          </div>
          
          <!-- 商品标题和描述 -->
          <div class=" mr-20">
            <h3 class="text-sm font-medium text-gray-800 mb-1">{{ item.title }}</h3>
            <p class="text-xs text-gray-500">{{ item.content }}</p>
          </div>
        
          
          <!-- 价格 - 独立一列 -->
          <div class="flex-1 text-red-500 font-medium text-right">
            ¥{{ item.price }}
          </div>
          
          <!-- 数量控制和删除 -->
          <div class="flex-1 ml-4">
            <div class="flex items-center justify-end">
              <div class="flex items-center border border-gray-300 rounded">
                <button 
                  class="px-2 py-1 text-gray-500" 
                  @click="decreaseQuantity(item,index)"
                  :disabled="item.count <= 1"
                >−</button>
                <input 
                  type="text" 
                  class="w-10 text-center border-x border-gray-300" 
                  v-model="item.count"
                  @input="validateQuantity(item,index)"
                />
                <button 
                  class="px-2 py-1 text-gray-500" 
                  @click="increaseQuantity(item,index)"
                >+</button>
              </div>
              <button 
                class="ml-2 px-2 py-1 border border-gray-300 rounded text-gray-600 text-sm"
                @click="removeItem(item,index)"
              >删除</button>
            </div>
          </div>
        </div>
      </div>
    </div>

    <!-- 底部结算栏 -->
    <div class="mt-6 flex justify-end items-center">
      <div class="text-sm text-gray-600 pr-16">
        商品数量: {{ selectedCount }}
      </div>
      <div class="flex items-center">
        <div class="mr-4 text-sm">
          合计: <span class="text-red-500 font-medium">¥{{ totalPrice }}</span>
        </div>
        <button 
          class="bg-green-700 text-white px-6 py-2 rounded flex items-center"
          :disabled="selectedCount === 0"
          @click="checkout"
        >
          <span class="mr-1">结算</span>
        </button>
      </div>
    </div>
    </div>
        <!-- 地址选择弹窗 -->
    <div v-if="showAddressModal" class="fixed inset-0 bg-black bg-opacity-50 flex items-center justify-center z-50">
      <div class="bg-white rounded-lg max-w-md w-full max-h-[80vh] overflow-y-auto">
        <div class="p-6 border-b border-gray-200 flex justify-between items-center">
          <h3 class="text-lg font-semibold">选择收货地址</h3>
          <button @click="showAddressModal = false" class="text-gray-500 hover:text-gray-700">
            <svg xmlns="http://www.w3.org/2000/svg" class="h-6 w-6" fill="none" viewBox="0 0 24 24" stroke="currentColor">
              <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M6 18L18 6M6 6l12 12" />
            </svg>
          </button>
        </div>
        
        <div class="p-4">
          <div 
            v-for="(address, index) in addresses" 
            :key="index"
            @click="selectAddress(index)"
            class="p-4 border rounded-md mb-3 cursor-pointer hover:border-red-500"
            :class="{'border-red-500': address === currentAddress}"
          >
            <div class="flex justify-between">
              <span class="font-medium">{{ address.consignee }}</span>
              <span>{{ address.phone }}</span>
            </div>
            <p class="text-gray-700 mt-1">{{ address.addressDetail }}</p>
          </div>
        </div>
        
        <div class="p-4 border-t border-gray-200">
          <button 
            @click="showAddressModal = false;changeAdd()" 
            class="w-full bg-red-600 text-white py-2 rounded-md font-medium hover:bg-red-700"
          >
            确认
          </button>
        </div>
      </div>
    </div>
 </section>
</template>

<script setup>
import { ref, computed } from 'vue';
import { onMounted } from "vue";
import { ElMessage, ElMessageBox } from 'element-plus';
import { apiClient } from "../api/apiService.js";
import { useRouter } from "vue-router";

import jixinguo from "@/assets/img/jixinguo.jpg";

const router = useRouter();
// 购物车商品数据
const cartItems = ref([0]);

const addressId= ref('');
const addressName = ref('');
const addressPhone = ref('');
const addressAddress= ref('');

// 地址选择弹窗显示状态
const showAddressModal = ref(false);


// Auto-rotate carousel
onMounted(async() => {

  //编辑展示的银行产品信息
  const shopCartGoodsData = await selectShopCartGoods();
    shopCartGoodsData.forEach((item) => {
      item['selected'] = false;
  });

  cartItems.value = shopCartGoodsData;

  //获取地址
  selectDefaultAssress();
});

// 购物车查询接口
const selectShopCartGoods = async () => {
  try {
    const response = await apiClient.get("/cart/show", {
      headers: {
        Authorization: window.localStorage.token,
      },
    });
    if (response.flag == true) {
      return response.data;
    }
    else {
      ElMessage.error('您未登录，请先登录');
    }
    
  } catch (error) {
    console.error("请求失败", error);
    throw error;
  }
};

// 查询默认地址接口
const selectDefaultAssress = async () => {
  try {
    const response = await apiClient.get("/address/selectDefaultByOwnName", {
      headers: {
        Authorization: window.localStorage.token,
      },
    });
    if (response.flag == true) {
      addressName.value = response.data.consignee;
      addressPhone.value = response.data.phone;
      addressAddress.value = response.data.addressDetail;
      addressId.value = response.data.id;

    }
    else {
      ElMessage.error('您未登录，请先登录');
    }
    
  } catch (error) {
    console.error("请求失败", error);
    throw error;
  }
};

// 更新购物车商品数量接口
const UpdShopCartGoods = async (shoppingId,count) => {
  try {
    const response = await apiClient.put(`/cart/update/${shoppingId}/${count}`, null, {
      headers: {
        Authorization: window.localStorage.token
      }
    });
    if (response.flag == true) {
      return response.data;
    }
    else {
      ElMessage.error('您未登录，请先登录');
    }
    
  } catch (error) {
    console.error("请求失败", error);
    throw error;
  }
};
// 删除购物车商品接口
const delShopCartGoods = async (shoppingId) => {
  try {
    const response = await apiClient.delete(`/cart/delete/${shoppingId}`, {
      headers: {
        Authorization: window.localStorage.token,
        "Content-Type": "application/json"
      }
    });
    console.log('Delete response:', response); // Debug logging
    if (response.flag == true) {
      return true;
    }
    else {
      ElMessage.error('您未登录，请先登录');
      return false;
    }
    
  } catch (error) {
    console.error("请求失败", error);
    throw error;
  }
};


// 全选状态
const selectAll = ref(false);

// 计算总商品数
const totalItems = computed(() => cartItems.value.length);

// 计算已选商品数量
const selectedCount = computed(() => {
  return cartItems.value.filter(item => item.selected).reduce((sum, item) => sum + item.count, 0);
});

// 计算总价
const totalPrice = computed(() => {
  return cartItems.value
    .filter(item => item.selected)
    .reduce((sum, item) => sum + item.price * item.count, 0);
});

// 全选/取消全选
const toggleSelectAll = () => {
  cartItems.value.forEach(item => {
    item.selected = selectAll.value;
  });
};

// 增加商品数量
const increaseQuantity = (item,index) => {
  cartItems.value[index].count++;
  UpdShopCartGoods(item.shoppingId,cartItems.value[index].count);
};

// 减少商品数量
const decreaseQuantity = (item,index) => {
  if (cartItems.value[index].count > 1) {
    cartItems.value[index].count--;
    UpdShopCartGoods(item.shoppingId,cartItems.value[index].count);
  }
};

// 验证输入的数量
const validateQuantity = (item,index) => {
  let count = parseInt(cartItems.value[index].count);
  if (isNaN(count) || count < 1) {
    cartItems.value[index].count = 1;
  } else {
    cartItems.value[index].count = count;
  }
  UpdShopCartGoods(item.shoppingId,cartItems.value[index].count);
};

// 删除单个商品
const removeItem = async (item,index) => {
  try {
    await ElMessageBox.confirm(
      '确定要删除该商品吗?',
      '提示',
      {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning',
      }
    )
    
    if (delShopCartGoods(item.shoppingId)){
      cartItems.value.splice(index, 1);
    }

  } catch {
    // User clicked cancel
  }
};

// 删除选中的商品
const deleteSelected = async () => {
  try {

    await ElMessageBox.confirm(
      '确定要删除该商品吗?',
      '提示',
      {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning',
      }
    )
    // Delete all selected items from backend first
    await Promise.all(
      cartItems.value
        .filter(item => item.selected)
        .map(item => delShopCartGoods(item.shoppingId))
    );
    
    // Then remove from local state
    //编辑展示的银行产品信息
    const shopCartGoodsData = await selectShopCartGoods();
      shopCartGoodsData.forEach((item) => {
        item['selected'] = false;
    });

    cartItems.value = shopCartGoodsData;
  } catch (error) {
    console.error("删除选中商品失败:", error);
    ElMessage.error('删除选中商品失败');
  }
};

// 地址数据
const addresses = ref([0]);
const currentAddress = ref([0]);

// 选择地址
const selectAddress = (index) => {
  currentAddress.value = addresses.value[index];
};

//检索所有地址
const showAllAddress= async () => {
  try {
    const response = await apiClient.get("/address/selectByOwnName", {
      headers: {
        Authorization: window.localStorage.token,
      },
    });
    if (response.flag == true) {
      addresses.value = response.data;
      // 当前选中的地址
      currentAddress.value = addresses.value[0];
    }
    else {
      ElMessage.error('您未登录，请先登录');
    }
    
  } catch (error) {
    console.error("请求失败", error);
    throw error;
  }
};

// 结算
const checkout = async() => {
  const orderList = cartItems.value.filter(item => item.selected);
  const param = orderList;

  try {
    const response = await apiClient.post(`/cart/commitOrder/${addressId.value}/${totalPrice.value}`, param, {
      headers: {
        Authorization: window.localStorage.token,
      },
    });
    
    if (response.flag) {
      ElMessage.success("订单提交成功");
      cartItems.value = cartItems.value.filter(item => !item.selected);
    } else {
      ElMessage.error(response.message);
    }
  } catch (error) {
    ElMessage.error("订单提交失败，请重试");
  }
};
const goBack = () => {
  // 返回时携带当前页面状态（需配合路由的state配置）
  router.back();
};
const changeAdd = (item) => {
  addressName.value = currentAddress.value.consignee;
  addressPhone.value = currentAddress.value.phone;
  addressAddress.value= currentAddress.value.addressDetail;
  addressId.value= currentAddress.value.id;
};

</script>
