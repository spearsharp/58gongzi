package com.qucai.sample.daifudemo.src.com.chinaebi.pay.security;

import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.cert.X509Certificate;

public class SignedPack {
    private byte[] signData;
    private PublicKey pubKey;
    private X509Certificate cert;
    private PrivateKey priKey;

    public SignedPack() {
    }

    public X509Certificate getCert() {
        return this.cert;
    }

    public void setCert(X509Certificate cert) {
        this.cert = cert;
    }

    public PublicKey getPubKey() {
        return this.pubKey;
    }

    public void setPubKey(PublicKey pubKey) {
        this.pubKey = pubKey;
    }

    public byte[] getSignData() {
        return this.signData;
    }

    public void setSignData(byte[] signData) {
        this.signData = signData;
    }

    public PrivateKey getPriKey() {
        return this.priKey;
    }

    public void setPriKey(PrivateKey priKey) {
        this.priKey = priKey;
    }
}
