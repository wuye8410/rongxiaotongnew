import { createRouter, createWebHistory } from 'vue-router'
import Home from '../views/Home.vue'
import front from '../views/FrontPage.vue'
import financing from '../views/FinancingPage.vue'
import AImatch from '../views/AIMatch.vue'
import ExpertAssis from '../views/ExpertAssistance.vue'
import AgriPro from '../views/AgriculturalProduct.vue'
import ShopCart from '../views/ShoppingCart.vue'
import AgriKnlg from '../views/AgriculturalKnowledge.vue'
import Question from '../views/QuestionDetails.vue'
import Personal from '../views/PersonalInfo.vue'
import MyMsg from '../components/MyMsg.vue'
import MyExpertMsg from '../components/MyExpertMsg.vue'
import MyPassword from '../components/MyPassword.vue'
import MyAddress from '../components/MyAddress.vue'
import MyGoods from '../components/MyGoods.vue'
import MyNeeds from '../components/MyNeeds.vue'
import MySellOrder from '../components/MySellOrder.vue'
import MyBuyOrder from '../components/MyBuyOrder.vue'
import MyQuestion from '../components/MyQuestion.vue'
import MyAppointment from '../components/MyAppointment.vue'
import MyKnowledge from '../components/MyKnowledge.vue'
import MyFinancing from '../components/MyFinancing.vue'
import MyUserManagement from '../components/MyUserManagement.vue'
import MyGoodsManagement from '../components/MyGoodsManagement.vue'
import FinancingList from '../views/FinancingList.vue'
import GoodsDetail from '../views/GoodsDetail.vue'

import loginpage from '../views/LoginPage.vue'
import registerpage from '../views/RegisterPage.vue'

// import MyAddress from '../components/MyAddress.vue'


const routes = [
  {
     path: '/',
    redirect: '/home'
  },
  {
    path: '/home',
    component: Home,
    children: [
      {
        path: '',
        component: front,
      },
      {
        path: '/financing',
        component: financing
      },
      {
        path: '/aimatch',
        component: AImatch
      },
      {
        path: '/expertassist',
        component: ExpertAssis
      },
      {
        path: '/agripro',
        component: AgriPro
      },
      {
        path: '/shopcart',
        component: ShopCart
      },
      {
        path: '/agriknlg',
        component: AgriKnlg
      },
      {
        path: '/question',
        component: Question
      },
      {
        path: '/financinglist',
        component: FinancingList
      },
      {
        path: '/goodsDetail',
        component: GoodsDetail
      },
      {
        path: '/personal',
        component: Personal,
        children: [
          {
            path: '',
            component: MyMsg
          },
          {
            path: 'mymsg',
            component: MyMsg
          },
          {
            path: 'myexpertmsg',
            component: MyExpertMsg
          },
          {
            path: 'mypwd',
            component: MyPassword
          },
          {
            path: 'myadd',
            component: MyAddress
          },
          {
            path: 'products',
            component: MyGoods
          },
          {
            path: 'requests',
            component: MyNeeds
          },
          {
            path: 'mysellorder',
            component: MySellOrder
          },
          {
            path: 'mybuyorder',
            component: MyBuyOrder
          },
          {
            path: 'myquestion',
            component: MyQuestion
          },
          {
            path: 'myappointment',
            component: MyAppointment
          },
          {
            path: 'myknowledge',
            component: MyKnowledge
          },
          {
            path: 'myfinancing',
            component: MyFinancing
          },
          {
            path: 'myusermng',
            component: MyUserManagement
          },
          {
            path: 'mygoodsmng',
            component: MyGoodsManagement
          },
        ]
      },
     ]
   },
   {
     path: '/login',
    component: loginpage,
  },
  {
     path: '/register',
    component: registerpage,
  },
]

const router = createRouter({
  history: createWebHistory(),
  routes
})

export default router 