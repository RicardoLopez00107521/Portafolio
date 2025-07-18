import { createRouter, createWebHistory } from 'vue-router';
import LoginView from '/src/views/LoginView.vue';
import MainView from '/src/views/MainView.vue';
import QRView from '/src/views/QRView.vue';
import MyHomeView from '/src/views/MyHomeView.vue';
import EntryView from '/src/views/EntryView.vue';
import PermissionView from '/src/views/PermissionView.vue';
import AdminView from '/src/views/AdminView.vue';
import GraphView from '/src/views/GraphView.vue';
import AdminHouseView from '/src/views/AdminHouseView.vue';
import AdminScanerView from '/src/views/AdminScanerView.vue';
import AdminVigilantView from '/src/views/AdminVigilantView.vue';
import AdminHouseDetailView from '@/views/AdminHouseDetailView.vue';
import VigilantView from '@/views/VigilantView.vue';
import ScanQRView from '@/views/ScanQRView.vue';
import CreateEntryView from '@/views/CreateEntryView.vue';
import VisitantePermissionView from '/src/views/VisitantePermissionView.vue';

const routes = [
  { path: '/', component: LoginView },
  {
    path: '/mainView',
    component: MainView,
    children: [
      { path: '', component: QRView, meta: { requiresAuth: true } },
      { path: 'myHomeView', component: MyHomeView, meta: { requiresAuth: true } },
      { path: 'entryView', component: EntryView, meta: { requiresAuth: true } },
      { path: 'permissionView', component: PermissionView},
      { path: 'adminView', component: AdminView, meta: { requiresAuth: true } },
      { path: 'graphView', component: GraphView, meta: { requiresAuth: true } },
      { path: 'adminHouseView', component: AdminHouseView, meta: { requiresAuth: true } },
      { path: 'adminScanerView', component: AdminScanerView, meta: { requiresAuth: true } },
      { path: 'adminVigilantView', component: AdminVigilantView, meta: { requiresAuth: true } },
      { path: 'adminHouseDetailView', component: AdminHouseDetailView, meta: { requiresAuth: true } },
      { path: 'visitantePermissionView', component: VisitantePermissionView, meta: { requiresAuth: true } },
      { path: 'scanner', component: ScanQRView, meta: { requiresAuth: true } },
      { path: 'createEntryView', component: CreateEntryView, meta: { requiresAuth: true } },
    ]
  }
];

const router = createRouter({
  history: createWebHistory(),
  routes,
});

// Middleware de navegación para verificar la autenticación
router.beforeEach((to, from, next) => {
  // Verificar si la ruta requiere autenticación
  if (to.meta.requiresAuth) {
    // Verificar si hay un token en el localStorage
    const token = localStorage.getItem('token');
    if (!token) {
      // Si no hay token, redirigir a la vista de login
      next('/');
    } else {
      // Si hay token, continuar navegación
      next();
    }
  } else {
    // Si la ruta no requiere autenticación, continuar navegación
    next();
  }
});

export default router;
