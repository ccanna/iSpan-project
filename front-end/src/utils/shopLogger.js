const isDev = import.meta.env.DEV

export const shopLog = (...args) => {
    if (isDev) {
        // 保護商城資料log資料
        return
    }
}