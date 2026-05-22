package id.my.sendiko.sembako.store.list.presentation

import id.my.sendiko.sembako.store.core.domain.Store
import id.my.sendiko.sembako.user.core.domain.User

data class StoreListState(
    val isLoading: Boolean = false,
    val message: String = "",
    val isError: Boolean = false,
    val stores: List<Store> = emptyList(),
    val user: User = User(
        id = 0,
        username = "",
        email = "",
        profileUrl = "",
        hasStore = false
    ),
    val isStoreSheetVisible: Boolean = false,
    val storeName: String = "",
    val storeAddress: String = "",
    val storePhone: String = "",
    val storeEmail: String = "",
    val selectedStoreId: Int? = null,
    val isDeleteDialogOpen: Boolean = false
)
