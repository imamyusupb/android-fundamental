package com.codeyuba.bfaa3submission.data.remote.response

import com.codeyuba.bfaa3submission.data.remote.response.UserItem
import com.google.gson.annotations.SerializedName

data class UserListResponse(

	@field:SerializedName("total_count")
	val totalCount: Int,

	@field:SerializedName("incomplete_results")
	val incompleteResults: Boolean,

	@field:SerializedName("items")
	val users: List<UserItem>
)

