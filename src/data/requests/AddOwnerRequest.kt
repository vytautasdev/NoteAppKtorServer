package com.noteapp.data.requests

data class AddOwnerRequest(
    val noteId: String,
    val owner: String
)