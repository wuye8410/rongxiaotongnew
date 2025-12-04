<template>
  <!-- 主内容区 -->
  <div class="mx-auto px-20">
    <!-- 我买到的 -->
    <div class="space-y-2">
      <!-- 顶部发布按钮 -->
      <div class="mb-4">
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
          <span>发布知识</span>
        </button>
      </div>

      <!-- 卡片网格布局 -->
      <div class="grid grid-cols-1 md:grid-cols-2 gap-6">
        <div
          v-for="(item, index) in plantingItems"
          :key="index"
          class="flex bg-white rounded-md shadow-sm overflow-hidden"
        >
          <!-- 左侧图片 -->
          <div class="w-1/3">
            <img
              :src="$store.state.imgShowRoad + '/file/order/' + item.picPath"
              :alt="item.title"
              class="w-full h-full object-cover"
            />
          </div>

          <!-- 右侧内容 -->
          <div class="w-2/3 p-4 relative">
            <h3 class="text-lg font-medium text-gray-800">{{ item.title }}</h3>
            <p class="text-sm text-gray-700 mb-1 line-clamp-3">
              {{ item.content }}
            </p>

            <!-- 底部操作按钮 -->
            <div class="flex space-x-2  justify-end">
                <button @click="proDataChange(item);showPublishDialog = true" class="text-blue-500 hover:text-blue-700">
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
                <button class="text-red-500 hover:text-red-700" @click="deleteGoods(item)">
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
        </div>
       
      </div>
       <div v-if="plantingItems.length === 0" class="px-3 py-10 text-center text-gray-500">        
      暂无数据        
      </div>
      

    </div>
    <!-- 添加/编辑表单弹窗 -->
    <!-- 发布商品弹窗 -->
    <el-dialog v-model="showPublishDialog" title="发布知识" width="600px">
      <el-form :model="publishForm" label-width="80px">
        <el-form-item required>
          <el-upload
            ref="uploadRef"
            action="#"
            list-type="picture-card"
            :on-change="handleImageUpload"
            :auto-upload="false"
            :limit="1"
            :file-list="
              publishForm.picPath
                ? [
                    {
                      name: 'order',
                      url:
                        $store.state.imgShowRoad +
                        '/file/order/' +
                        publishForm.picPath,
                    },
                  ]
                : []
            "
          >
            <el-icon><PlusIcon /></el-icon>
          </el-upload>
        </el-form-item>
        <el-form-item label="标题" required>
          <el-input v-model="publishForm.title" placeholder="请输入标题" />
        </el-form-item>
        <el-form-item label="内容" required>
          <el-input
            v-model="publishForm.content"
            type="textarea"
            :rows="4"
            placeholder="请输入详细内容"
          />
        </el-form-item>
      </el-form>
      <template #footer>
        <span class="dialog-footer">
          <el-button @click="handleCancel">取消</el-button>
          <el-button type="primary" @click="submitPublish"> 发布 </el-button>
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
import { ElMessage } from "element-plus";
import { useStore } from "vuex";

const store = useStore();
const router = useRouter();
// 初始数据
const plantingItems = ref([]);

// 发布商品相关
const showPublishDialog = ref(false);
const currentData = ref();
const publishForm = ref({
  knowledgeId: "",
  picPath: "",
  title: "",
  content: "",
  images: [],
});

const handleImageUpload = (file) => {
  publishForm.value.images.push(file);
};

const fileInfo = ref("");

const uploadRef = ref();

const handleCancel = () => {
  showPublishDialog.value = false;
  publishForm.value = {
    knowledgeId: "",
    picPath: "",
    title: "",
    content: "",
    images: [],
  };
  fileInfo.value = '';
  if (uploadRef.value) {
    uploadRef.value.clearFiles();
  }
};

const submitPublish = async () => {
  if (!publishForm.value.title || !publishForm.value.content) {
    ElMessage.error("请完整填写信息");
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
        fileInfo.value = response.data.split("/")[1];
      } else {
        ElMessage.error("发布失败");
      }
    }
    //判断是新增还是修改
    if (publishForm.value.knowledgeId != "") {
      const param = ref({
        knowledgeId: publishForm.value.knowledgeId,
        title: publishForm.value.title,
        content: publishForm.value.content,
        picPath: fileInfo.value ? fileInfo.value : publishForm.value.picPath,
        ownName: currentData.ownName,
        createTime: currentData.createTime,
        updateTime: currentData.updateTime,
      });
      //发布商品
      const responseOrder = await apiClient.put(
        `/knowledge/${publishForm.value.knowledgeId}`,
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
        content: publishForm.value.content,
        picPath: fileInfo.value,
      });
      //发布商品
      const responseOrder = await apiClient.post(`/knowledge`, param.value, {
        headers: {
          Authorization: window.localStorage.token,
        },
      });

      if (responseOrder.flag) {
        ElMessage.success("发布成功");
      } else {
        ElMessage.error("发布失败");
      }
    }
    showPublishDialog.value = false;
    // 重置表单
    publishForm.value = {
      knowledgeId: "",
      picPath: "",
      title: "",
      content: "",
      images: [],
    };
    fileInfo.value = '';
    if (uploadRef.value) {
      uploadRef.value.clearFiles();
    }

    loadData({
    });
  } catch (error) {
    console.error("发布失败:", error);
    ElMessage.error("发布失败");
  }
};

// 删除地址
const deleteGoods = async (item) => {
  try {
    if (confirm("确定要删除吗？")) {
      //删除
      const response = await apiClient.delete(`/knowledge/${item.knowledgeId}`, {
        headers: {
          Authorization: window.localStorage.token,
        },
      });
      console.log("Delete response:", response); // Debug logging
      if (response.flag == true) {
        ElMessage.success("删除成功。");
      } else {
        ElMessage.error("删除失败，请重试。");
      }

      //重新加载商品
      loadData({});
    }
  } catch (error) {
    console.error("删除失败，请重试:", error);
  }
};

const proDataChange = (item) => {
  publishForm.value = {
    knowledgeId: item.knowledgeId,
    picPath: item.picPath,
    title: item.title,
    content: item.content,
    images: [],
  };
  currentData.value = item;
};


// 后端请求数据数据
const selectData = async ({  }) => {
  try {
    //检索URL
    const url = ref(`/knowledge/selectByUsername`);

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
const loadData = async ({  }) => {
  try {
    const res = await selectData({  });
    plantingItems.value = res;
  } catch (error) {
    console.error("数据加载失败:", error);
  }
};


// 初始化加载
onMounted(() => {
  loadData({
  });
});

</script>
