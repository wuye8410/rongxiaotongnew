<template>
  <!-- 主内容区 -->
  <div class="mx-auto px-20">
    <!-- 搜索栏 -->
    <div class="container mx-auto px-4 pb-4">
      <div class="flex items-center justify-center">
        <input
          type="text"
          v-model="searchKey"
          placeholder="输入关键词搜索"
          class="border border-gray-300 rounded-l px-4 py-2 w-2/3 focus:outline-none"
        />
        <button
          class="bg-[#007029] text-white mx-2 px-4 py-2 rounded-md flex items-center" @click="searchGoods()"
        >
          搜索
        </button>

        <button
          @click="showPublishDialog = true" 
          class="flex items-center space-x-2 bg-[#007029] hover:bg-green-800 text-white font-medium py-2 px-4 rounded-md focus:outline-none focus:ring-2 focus:ring-green-500 focus:ring-offset-2"
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
            class="lucide lucide-plus"
          >
            <path d="M5 12h14" />
            <path d="M12 5v14" />
          </svg>
          <span>发布需求</span>
        </button>
      </div>
    </div>
    <!-- 商品货源 -->
    <div class="space-y-2">
      <div class="grid gap-4 md:grid-cols-3">
        <div
          v-for="(product, index) in needsData"
          :key="index"
          class="border rounded-lg overflow-hidden"
        >
          <div class="h-40 bg-gray-100">
            <img
              :src="$store.state.imgShowRoad + '/file/order/' + product.picture"
              alt="需求图片"
              class="w-full h-full object-cover"
            />
          </div>
          <div class="p-4">
            <div class="flex space-x-2 items-center justify-between">
              <h3 class="font-medium text-lg mb-2">{{ product.title }}</h3>
              <div class="flex space-x-2 items-center justify-end">
                <button
                  @click="proDataChange(product);showPublishDialog = true" 
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
                    <path
                      d="M17 3a2.85 2.83 0 1 1 4 4L7.5 20.5 2 22l1.5-5.5Z"
                    />
                    <path d="m15 5 4 4" />
                  </svg>
                </button>
                <button class="text-red-500 hover:text-red-700" @click="deleteGoods(product)">
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
            </div>
            <p class="text-gray-600 text-sm mb-2 line-clamp-2">
              {{ product.content }}
            </p>
          </div>
        </div>
      </div>
          <div v-if="needsData.length === 0" class="px-3 py-10 text-center text-gray-500">        
      暂无数据        
      </div>

      <!-- 分页 -->
        <div class="flex justify-center items-center mt-8 text-sm">
    <Pagination
      :current-page="pagination.currentPage"
      :page-size="pagination.pageSize"
      :total="pagination.total"
      @update:current-page="setCurrentPage"
      @update:page-size="setPageSize"
      @page-change="loadData"
      class="mt-6"
    />
  </div>
    </div>

    <!-- 发布商品弹窗 -->
    <el-dialog v-model="showPublishDialog" title="发布需求" width="600px">
      <el-form :model="publishForm" label-width="80px">
        <el-form-item label="商品图片" required>
          <el-upload
            ref="uploadRef"
            action="#"
            list-type="picture-card"
            :on-change="handleImageUpload"
            :auto-upload="false"
            :limit="1"
            :file-list="
              publishForm.pic
                ? [
                    {
                      name: 'order',
                      url: $store.state.imgShowRoad + '/file/order/' + publishForm.pic,
                    },
                  ]
                : []
            "
            
          >
            <el-icon><PlusIcon /></el-icon>
          </el-upload>
        </el-form-item>
        <el-form-item label="商品标题" required>
          <el-input v-model="publishForm.title" placeholder="请输入商品标题" />
        </el-form-item>
        <el-form-item label="详细介绍" required>
          <el-input 
            v-model="publishForm.description" 
            type="textarea" 
            :rows="4" 
            placeholder="请输入商品详细介绍"
          />
        </el-form-item>
        <el-form-item label="定价" required>
          <el-input-number 
            v-model="publishForm.price" 
            :min="0" 
            :precision="2"
            placeholder="请输入商品价格"
          />
        </el-form-item>
      </el-form>
      <template #footer>
        <span class="dialog-footer">
          <el-button @click="handleCancel">取消</el-button>
          <el-button type="primary" @click="submitPublish">
            发布
          </el-button>
        </span>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, reactive } from "vue";
import {
  ShoppingCartIcon,
  UserIcon,
  SearchIcon,
  PlusIcon,
} from "lucide-vue-next";
import { onMounted } from "vue";
import { defineComponent } from "vue";
import { apiClient } from "../api/apiService.js";
import { useRouter } from "vue-router";
import { ElMessage } from 'element-plus';
import { useStore } from "vuex";
import Pagination from "../components/Pagination.vue";

const store = useStore();
const router = useRouter();


// 检索关键字内容
const searchKey = ref("");
const searchFlag = ref(0);

// 菜单展开状态
const expandedMenus = reactive({
  myInfo: true,
  myPosts: false,
});

// 切换菜单展开状态
const toggleMenu = (menu) => {
  expandedMenus[menu] = !expandedMenus[menu];
};

// 发布商品相关
const showPublishDialog = ref(false);
const publishForm = ref({
  orderId:'',
  pic:'',
  title: '',
  description: '',
  price: '',
  images: []
});

const handleImageUpload = (file) => {
  publishForm.value.images.push(file);
};

const fileInfo = ref('');

const uploadRef = ref();

const handleCancel = () => {
  showPublishDialog.value = false;
  publishForm.value = {
    orderId:'',
    pic:'',
    title: '',
    description: '',
    price: '',
    images: []
  };
  if (uploadRef.value) {
    uploadRef.value.clearFiles();
  }
};

const submitPublish = async () => {
  if (!publishForm.value.title || !publishForm.value.price|| !publishForm.value.description) {
    ElMessage.error('请完整填写商品信息');
    return;
  }
  if (publishForm.value.title.length < 2 ||publishForm.value.title.length>8) {
    ElMessage.error('商品标题长度在2-8字之间');
    return;
  }
  if (publishForm.value.description.length < 10 ||publishForm.value.description.length>150) {
    ElMessage.error('详细介绍长度在10-150字之间');
    return;
  }

  try {
    // 上传图片
    const uploadedImages = [];
    for (const file of publishForm.value.images) {
      const formData = new FormData();
      formData.append("file", file.raw);


      //上传商品图片
      const response = await apiClient.post(
        `${store.state.fileUploadRoad}/file/upload/order`,
        formData,
        {
          headers: {
            Authorization: window.localStorage.token,
            "Content-Type": "multipart/form-data",
          },
        }
      );
      
      if (response.flag) {
        fileInfo.value = response.data.split('/')[1];
      } else {
        ElMessage.error("发布失败");
      }
    }
      //判断是新增还是修改商品
      try{
      if(publishForm.value.orderId != ''){
        const param = ref({
          order_id:publishForm.value.orderId,
            title: publishForm.value.title,
            content: publishForm.value.description,
            price: publishForm.value.price,
            picture: fileInfo.value?fileInfo.value:publishForm.value.pic
            })
        //发布商品
        const responseOrder = await apiClient.put(
          `/order/${publishForm.value.orderId}`,
          param.value,
          {
            headers: {
              Authorization: window.localStorage.token,
            },
          }
        );
        
        if (responseOrder.flag) {
          ElMessage.success("修改成功");
        } else {
          ElMessage.error("修改失败");
        }
      } else {
            const param = ref({
            title: publishForm.value.title,
            content: publishForm.value.description,
            price: publishForm.value.price,
            type: 'needs',
            picture: fileInfo.value
            })
        //发布商品
        const responseOrder = await apiClient.post(
          `/order`,
          param.value,
          {
            headers: {
              Authorization: window.localStorage.token,
            },
          }
        );
        
        if (responseOrder.flag) {
          ElMessage.success("发布成功");
        } else {
          ElMessage.error("发布失败");
        }
      }
      }catch (error) {
    console.error('删除地址失败，请重试:', error);
  }
    showPublishDialog.value = false;
    // 重置表单
    publishForm.value = {
      orderId:'',
      pic:'',
      title: '',
      description: '',
      price: '',
      images: []
    };
    if (uploadRef.value) {
    uploadRef.value.clearFiles();
  }

  searchKey.value = "";
  searchFlag.value = 0;
  pagination.value.currentPage = 1;
  loadData({
    page: pagination.value.currentPage,
  });


  } catch (error) {
    console.error('发布商品失败:', error);
    ElMessage.error('发布商品失败');
  }
};

// 删除地址
const deleteGoods = async(item) => {
  try{
    if (confirm("确定要删除商品吗？")) {
      //删除
      const response = await apiClient.delete(`/order/${item.orderId}`, {
        headers: {
          Authorization: window.localStorage.token,
        }
      });
      console.log('Delete response:', response); // Debug logging
      if (response.flag == true) {
        ElMessage.success("删除商品成功。");
      }
      else {
        ElMessage.error("删除商品失败，请重试。");
      }

      //重新加载商品
      searchKey.value = "";
      searchFlag.value = 0;
      pagination.value.currentPage = 1;
      loadData({
        page: pagination.value.currentPage,
      });
    }
  }  catch (error) {
    console.error('删除地址失败，请重试:', error);
  }
};

// 商品货源数据
const products = ref([]);

const proDataChange = (item)=> {
  publishForm.value = {
    orderId:item.orderId,
    pic:item.picture,
    title: item.title,
    description: item.content,
    price: item.price,
    images: []
  };
};

// 分页状态
const pagination = ref({
  currentPage: 1,
  pageSize: 28,
  total: 0,
});

// 表格数据
const needsData = ref([]);

// 后端请求数据数据
const selectData = async ({ page }) => {
  try {
    //检索URL
    const url1=ref(`/order/search/needs/${page}`);
    const url2=ref(`/order/searchMyNeedsByKeys/${searchKey.value}/${page}`);
    const url = ref("");
    
    if (searchFlag.value == 1 && searchKey.value) {
      url.value = url2.value;
    } else {
      url.value = url1.value;
    }
    const response = await apiClient.get(url.value, {
      headers: {
        Authorization: window.localStorage.token,
      },
    });
    if (response.flag) {
      return response.data;
    } else {
      ElMessage.error(response.data);
    }
  } catch (error) {
    console.error("请求失败", error);
    throw error;
  }
};

// 加载数据
const loadData = async ({ page }) => {
  try {
    const res = await selectData({ page });
    needsData.value = res.list;
    pagination.value.total = res.total;
  } catch (error) {
    console.error("数据加载失败:", error);
  }
};

// 更新当前页
const setCurrentPage = (page) => {
  pagination.value.currentPage = page;
};

// 更新每页条数
const setPageSize = (size) => {
  pagination.value.pageSize = size;
};

// 初始化加载
onMounted(() => {
  loadData({
    page: pagination.value.currentPage,
  });
});

//点击检索
const searchGoods = ()=> {
  pagination.value.currentPage = 1;
  searchFlag.value = 1;
  loadData({
    page: pagination.value.currentPage,
  });
};


</script>

<style scoped>
/* 可以添加额外的样式 */
</style>