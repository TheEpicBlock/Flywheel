package com.jozufozu.flywheel.util.transform;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Matrix3f;
import com.mojang.math.Matrix4f;
import com.mojang.math.Quaternion;

public class MatrixTransformStack implements TransformStack {

	private final PoseStack internal;

	public MatrixTransformStack() {
		this(new PoseStack());
	}

	public MatrixTransformStack(PoseStack internal) {
		this.internal = internal;
	}

	public PoseStack unwrap() {
		return internal;
	}

	public MatrixTransformStack setIdentity() {
		if (internal.clear()) {
			PoseStack.Pose last = internal.last();

			last.normal()
					.setIdentity();
			last.pose()
					.setIdentity();
		} else {
			internal.popPose();
			internal.pushPose();
		}

		return this;
	}

	@Override
	public TransformStack translate(double x, double y, double z) {
		internal.translate(x, y, z);
		return this;
	}

	@Override
	public TransformStack multiply(Quaternion quaternion) {
		internal.mulPose(quaternion);
		return this;
	}

	@Override
	public TransformStack scale(float factorX, float factorY, float factorZ) {
		internal.scale(factorX, factorY, factorZ);
		return this;
	}

	@Override
	public TransformStack pushPose() {
		internal.pushPose();
		return this;
	}

	@Override
	public TransformStack popPose() {
		internal.popPose();
		return this;
	}

	@Override
	public TransformStack mulPose(Matrix4f pose) {
		internal.last().pose().multiply(pose);
		return this;
	}

	@Override
	public TransformStack mulNormal(Matrix3f normal) {
		internal.last().normal().mul(normal);
		return this;
	}
}
