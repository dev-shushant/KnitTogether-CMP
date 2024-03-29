package dev.shushant.permission.picker.ios

import dev.shushant.permission.Permission
import dev.shushant.permission.PermissionsController
import dev.shushant.permission.data.AppBitmap
import dev.shushant.permission.data.FileMedia
import dev.shushant.permission.data.Media
import dev.shushant.permission.picker.AdaptivePresentationDelegateToContinuation
import dev.shushant.permission.picker.DEFAULT_MAX_IMAGE_HEIGHT
import dev.shushant.permission.picker.DEFAULT_MAX_IMAGE_WIDTH
import dev.shushant.permission.picker.DocumentPickerDelegateToContinuation
import dev.shushant.permission.picker.ImagePickerDelegateToContinuation
import dev.shushant.permission.picker.MediaSource
import platform.CoreServices.kUTTypeData
import platform.CoreServices.kUTTypeImage
import platform.CoreServices.kUTTypeMovie
import platform.CoreServices.kUTTypeVideo
import platform.Foundation.CFBridgingRelease
import platform.UIKit.UIDocumentPickerMode
import platform.UIKit.UIDocumentPickerViewController
import platform.UIKit.UIImagePickerController
import platform.UIKit.UIImagePickerControllerSourceType
import platform.UIKit.UIViewController
import platform.UIKit.presentationController
import kotlin.coroutines.suspendCoroutine
import kotlin.native.ref.WeakReference

class MediaPickerController(
    override val permissionsController: PermissionsController
) : MediaPickerControllerProtocol {
    private val strongRefs: MutableSet<Any> = mutableSetOf()
    private lateinit var getViewController: () -> UIViewController

    @Suppress("unused")
    constructor(
        permissionsController: PermissionsController,
        viewController: UIViewController
    ) : this(
        permissionsController = permissionsController
    ) {
        bind(viewController)
    }

    @Suppress("unused")
    constructor(
        permissionsController: PermissionsController,
        getViewController: () -> UIViewController
    ) : this(
        permissionsController = permissionsController
    ) {
        this.getViewController = getViewController
    }

    override fun bind(viewController: UIViewController) {
        val weakRef: WeakReference<UIViewController> = WeakReference(viewController)
        this.getViewController = { weakRef.get() ?: error("viewController was deallocated") }
    }

    override suspend fun pickImage(source: MediaSource): AppBitmap {
        return pickImage(source, DEFAULT_MAX_IMAGE_WIDTH, DEFAULT_MAX_IMAGE_HEIGHT)
    }

    override suspend fun pickImage(source: MediaSource, maxWidth: Int, maxHeight: Int): AppBitmap {
        source.requiredPermissions().forEach { permission ->
            permissionsController.providePermission(permission)
        }

        val refs: MutableSet<Any> = mutableSetOf()
        strongRefs.add(refs)
        val media: Media = suspendCoroutine { continuation ->
            val controller = UIImagePickerController()
            controller.sourceType = source.toSourceType()
            controller.mediaTypes = listOf(kImageType)
            controller.delegate = ImagePickerDelegateToContinuation(continuation).also {
                refs.add(it)
            }
            getViewController().presentViewController(
                controller,
                animated = true,
                completion = null
            )
            controller.presentationController?.delegate =
                AdaptivePresentationDelegateToContinuation(continuation).also {
                    refs.add(it)
                }
        }
        strongRefs.remove(refs)
        return media.preview
    }

    override suspend fun pickFiles(): FileMedia {
        val refs: MutableSet<Any> = mutableSetOf()
        strongRefs.add(refs)

        val fileMedia: FileMedia = suspendCoroutine { continuation ->
            val controller = UIDocumentPickerViewController(
                documentTypes = listOf(kStandardFileTypesId),
                inMode = UIDocumentPickerMode.UIDocumentPickerModeImport
            )
            controller.delegate = DocumentPickerDelegateToContinuation(continuation).also {
                refs.add(it)
            }
            getViewController().presentViewController(
                controller,
                animated = true,
                completion = null
            )
            controller.presentationController?.delegate =
                AdaptivePresentationDelegateToContinuation(continuation).also {
                    refs.add(it)
                }
        }
        strongRefs.remove(refs)
        return fileMedia
    }

    private fun MediaSource.requiredPermissions(): List<Permission> =
        when (this) {
            MediaSource.GALLERY -> listOf(Permission.GALLERY)
            MediaSource.CAMERA -> listOf(Permission.CAMERA)
        }

    private fun MediaSource.toSourceType(): UIImagePickerControllerSourceType =
        when (this) {
            MediaSource.GALLERY -> UIImagePickerControllerSourceType.UIImagePickerControllerSourceTypePhotoLibrary
            MediaSource.CAMERA -> UIImagePickerControllerSourceType.UIImagePickerControllerSourceTypeCamera
        }

    override suspend fun pickMedia(): Media {
        permissionsController.providePermission(Permission.GALLERY)

        val refs: MutableSet<Any> = mutableSetOf()
        strongRefs.add(refs)

        val media: Media = suspendCoroutine { continuation ->
            val controller = UIImagePickerController()
            controller.sourceType =
                UIImagePickerControllerSourceType.UIImagePickerControllerSourceTypePhotoLibrary
            controller.mediaTypes = listOf(kImageType, kVideoType, kMovieType)
            controller.delegate = ImagePickerDelegateToContinuation(continuation).also {
                refs.add(it)
            }
            getViewController().presentViewController(
                controller,
                animated = true,
                completion = null
            )
        }
        strongRefs.remove(refs)

        return media
    }

    internal companion object {
        val kVideoType = CFBridgingRelease(kUTTypeVideo) as String
        val kMovieType = CFBridgingRelease(kUTTypeMovie) as String
        val kImageType = CFBridgingRelease(kUTTypeImage) as String
        val kStandardFileTypesId = CFBridgingRelease(kUTTypeData) as String
    }
}